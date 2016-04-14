//
//  MainViewController.m
//  SpotSome
//
//  Created by Tom Wieschalla  on 22.06.15.
//  Copyright (c) 2015 SpotSome@Beuth. All rights reserved.
//

#import "MainViewController.h"
#import "YLLocalSearch.h"
#import "YLLocalSearchResponse.h"
#import "YLBusiness.h"
#import "YelpTableViewController.h"
#import "SpotsomeSpotMainViewController.h"
#import "AppConstants.h"
#import "AppDelegate.h"
#import "SSpot.h"
#import "SLocation.h"
#import "HybridSpot.h"
#import "YelpAddSpotViewController.h"

@implementation MainViewController {
    NSMutableArray *yelpSpots;
    NSMutableArray *ourSpots;
    NSMutableArray *mergedSpots;
    AppDelegate *delegate;
    UIView *customView;
    UITextField *nameField;
    UITextField *range;
    
    bool started;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    
    started = YES;
    
    //start locating
    self.manager = [[CLLocationManager alloc] init];
    if ([self.manager respondsToSelector:@selector(requestWhenInUseAuthorization)]) {
        [self.manager requestWhenInUseAuthorization];
    }
    [self.manager startUpdatingLocation];

    [self.mapView setShowsUserLocation:YES];
    [self.mapView setMapType:MKMapTypeHybrid];
    [self.mapView setZoomEnabled:YES];
    [self.mapView setScrollEnabled:YES];
    self.mapView.delegate = self;
    
    self.title = @"Dein Standort";
    UIBarButtonItem *btn = [[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemAdd target:self action:@selector(openSpotCreationView)];
    [self.navigationItem setRightBarButtonItem:btn];
    
    [self.refreshButton.imageView setBounds:CGRectMake(0, 0, 10, 10)];
    
    delegate = [[UIApplication sharedApplication] delegate];
}

- (IBAction)refresh:(id)sender {
    YLLocalSearch *search = [[YLLocalSearch alloc] initWithMap:_mapView];

    [search localSearchWithLocation:self.mapView.userLocation
                            success:^(YLLocalSearchResponse *results) {
                                [_mapView removeAnnotations:[_mapView annotations]];
                                yelpSpots = [results businesses];
                                ourSpots = [self getSpots];
                                [self differSpotsWithYelp];
                                [self addSpotsAsAnnotationsToMapViewWithYelpSpots:yelpSpots ownSpots:ourSpots andMergedSpots:mergedSpots];
                            }
                            failure:^(NSError *error) {
                                NSLog(@"Oh noes! %@",error);
                            }];
}

#pragma mark - MKMAPKIT Delegate

-(void)mapView:(MKMapView *)mapView didUpdateUserLocation:(MKUserLocation *)userLocation {
    MKCoordinateRegion region;
    MKCoordinateSpan span;
    span.latitudeDelta = 0.001;
    span.longitudeDelta = 0.001;
    CLLocationCoordinate2D location;
    location.latitude = userLocation.coordinate.latitude;
    location.longitude = userLocation.coordinate.longitude;
    region.span = span;
    region.center = location;
    [self.mapView setRegion:region animated:YES];
    
    if (started) {
        [self refresh:nil];
        started = NO;
    }
    
}

-(void)mapView:(MKMapView *)mapView annotationView:(MKAnnotationView *)view calloutAccessoryControlTapped:(UIControl *)control {
    
    UIStoryboard *sb = [UIStoryboard storyboardWithName:@"Main" bundle:nil];
    
    if ([view.reuseIdentifier isEqualToString:@"Yelp"]) {
        YelpTableViewController *vc = (YelpTableViewController *)[sb instantiateViewControllerWithIdentifier:@"spotpush"];
        
        for (YLBusiness* b in yelpSpots) {
            
            NSArray *annos = self.mapView.selectedAnnotations;
            
            if (annos.count > 0) {
                MKPointAnnotation *point = annos[0];
                if ([point.title isEqualToString:b.id]) {
                    vc.tappedBusiness = b;
                    break;
                }
            }
        }
        vc.isHybrid = false;
        [self.navigationController pushViewController:vc animated:YES];
    } else if ([view.reuseIdentifier isEqualToString:@"Spotsome"]) {
        
        SpotsomeSpotMainViewController *vc = (SpotsomeSpotMainViewController*)[sb instantiateViewControllerWithIdentifier:@"spotmain"];
        
        for (SSpot *s in ourSpots) {
            
            NSArray *annos = self.mapView.selectedAnnotations;
            
            if (annos.count > 0) {
                MKPointAnnotation *point = annos[0];
                if ([point.title isEqualToString:s.name]) {
                    vc.spot = s;
                    NSDictionary *address = [self getAddressForLocationWithLatitude:[s.location.latitude doubleValue] andLongitude:[s.location.longitude doubleValue]];
                    vc.street = [address objectForKey:@"street"];
                    vc.zipcode = [address objectForKey:@"zipcode"];
                    vc.country = [address objectForKey:@"country"];
                    break;
                }
            }
        }
        
        [self.navigationController pushViewController:vc animated:YES];
        
    } else {
        YelpTableViewController *vc = (YelpTableViewController *)[sb instantiateViewControllerWithIdentifier:@"spotpush"];
        
        for (HybridSpot *h in mergedSpots) {
            
            NSArray *annos = self.mapView.selectedAnnotations;
            
            if (annos.count > 0) {
                MKPointAnnotation *point = annos[0];
                if ([point.title isEqualToString:h.yelpSpot.id]) {
                    vc.tappedBusiness = h.yelpSpot;
                    vc.spot = h.sspot;
                    break;
                }
            }
        }
        
        vc.isHybrid = true;
        [self.navigationController pushViewController:vc animated:YES];
    }

}

-(void)mapView:(MKMapView *)mapView didAddAnnotationViews:(NSArray *)views {
    
    for (MKAnnotationView *annView in views)
    {
        CGRect endFrame = annView.frame;
        annView.frame = CGRectOffset(endFrame, 0, -500);
        [UIView animateWithDuration:0.5
                         animations:^{ annView.frame = endFrame; }];
    }
}

-(MKAnnotationView *)mapView:(MKMapView *)mapView viewForAnnotation:(id<MKAnnotation>)annotation {
    
    if (annotation == mapView.userLocation) {
        return nil;
    }
    
    MKPointAnnotation *a = (MKPointAnnotation *)annotation;
    
    if ([a.subtitle isEqualToString:@"Yelp-Spot"]) {
        MKPinAnnotationView *pinView = [[MKPinAnnotationView alloc] initWithAnnotation:annotation reuseIdentifier:@"Yelp"];
        pinView.pinColor = MKPinAnnotationColorRed;
        pinView.animatesDrop = YES;
        pinView.frame = CGRectMake(0, 0, 10, 10);
        pinView.canShowCallout = YES;
        
        UIButton * rightButton = [UIButton buttonWithType:UIButtonTypeDetailDisclosure];
        [rightButton setImage:[UIImage new] forState:UIControlStateNormal];
        
        UITableViewCell *disclosure = [[UITableViewCell alloc] init];
        [rightButton addSubview:disclosure];
        disclosure.frame = rightButton.bounds;
        disclosure.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
        disclosure.userInteractionEnabled = NO;
        
        
        UIImageView *iconView = [[UIImageView alloc] initWithFrame:CGRectMake(0, 0, 50, 50)];
        [iconView setImage:[UIImage imageNamed:@"mapViewYelpSpot"]];
        
        pinView.leftCalloutAccessoryView = iconView;
        
        pinView.rightCalloutAccessoryView = rightButton;
        
        return pinView;
    } else if ([a.subtitle isEqualToString:@"Spotsome-Spot"]){
        MKPinAnnotationView *pinView = [[MKPinAnnotationView alloc] initWithAnnotation:annotation reuseIdentifier:@"Spotsome"];
        pinView.pinColor = MKPinAnnotationColorGreen;
        pinView.animatesDrop = YES;
        pinView.canShowCallout = YES;
        
        UIButton * rightButton = [UIButton buttonWithType:UIButtonTypeDetailDisclosure];
        [rightButton setImage:[UIImage new] forState:UIControlStateNormal];
        
        UITableViewCell *disclosure = [[UITableViewCell alloc] init];
        [rightButton addSubview:disclosure];
        disclosure.frame = rightButton.bounds;
        disclosure.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
        disclosure.userInteractionEnabled = NO;

        UIImageView *iconView = [[UIImageView alloc] initWithFrame:CGRectMake(0, 0, 50, 50)];
        [iconView setImage:[UIImage imageNamed:@"mapViewSpot"]];
        
        pinView.leftCalloutAccessoryView = iconView;
        
        pinView.rightCalloutAccessoryView = rightButton;
        
        return pinView;
    } else {
        MKPinAnnotationView *pinView = [[MKPinAnnotationView alloc] initWithAnnotation:annotation reuseIdentifier:@"Hybrid"];
        pinView.pinColor = MKPinAnnotationColorGreen;
        pinView.animatesDrop = YES;
        pinView.canShowCallout = YES;
        
        UIButton * rightButton = [UIButton buttonWithType:UIButtonTypeDetailDisclosure];
        [rightButton setImage:[UIImage new] forState:UIControlStateNormal];
        
        UITableViewCell *disclosure = [[UITableViewCell alloc] init];
        [rightButton addSubview:disclosure];
        disclosure.frame = rightButton.bounds;
        disclosure.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
        disclosure.userInteractionEnabled = NO;
        
        UIImageView *iconView = [[UIImageView alloc] initWithFrame:CGRectMake(0, 0, 50, 50)];
        [iconView setImage:[UIImage imageNamed:@"mapViewSpot"]];
        
        pinView.leftCalloutAccessoryView = iconView;
        
        pinView.rightCalloutAccessoryView = rightButton;
        
        return pinView;
    }
    

}

#pragma mark - Spot methods

-(void)addSpotsAsAnnotationsToMapViewWithYelpSpots:(NSArray *)yelpSpots ownSpots:(NSArray *)ownSpots andMergedSpots:(NSArray *)hybridSpots{
    
    for (YLBusiness* b in yelpSpots) {
        MKPointAnnotation *point = [MKPointAnnotation new];
        NSDictionary *coordinate = [b.location objectForKey:@"coordinate"];
        CLLocationCoordinate2D c2D = CLLocationCoordinate2DMake( [[coordinate objectForKey:@"latitude"]doubleValue],[[coordinate objectForKey:@"longitude"] doubleValue]);
        point.coordinate = c2D;
        point.title = b.id;
        point.subtitle = @"Yelp-Spot";
        
        [self.mapView addAnnotation:point];
    }
    
    for (SSpot *s in ownSpots) {
        MKPointAnnotation *point = [MKPointAnnotation new];
        CLLocationCoordinate2D c2D = CLLocationCoordinate2DMake( [s.location.latitude doubleValue],[s.location.longitude doubleValue]);
        point.coordinate = c2D;
        point.title = s.name;
        point.subtitle = @"Spotsome-Spot";
        [self.mapView addAnnotation:point];
    }
    
    for (HybridSpot *h in hybridSpots) {
        MKPointAnnotation *point = [MKPointAnnotation new];
        CLLocationCoordinate2D c2D = CLLocationCoordinate2DMake( [h.sspot.location.latitude doubleValue],[h.sspot.location.longitude doubleValue]);
        point.coordinate = c2D;
        point.title = h.yelpSpot.id;
        point.subtitle = @"Hybrid-Spot";
        [self.mapView addAnnotation:point];
    }
}

-(NSArray*)getSpots {
    
    //sending post request for auth in java backend
    NSString *urlString = [NSString stringWithFormat:@"%@/spot?latitude=%f&longitude=%f",BASEURL,self.mapView.userLocation.coordinate.latitude,self.mapView.userLocation.coordinate.longitude];
    
    NSError *error = nil;
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] initWithURL:[NSURL URLWithString:urlString]];
    NSURLResponse *response;
    [request setHTTPMethod:@"GET"];
    [request setValue:delegate.token forHTTPHeaderField:@"X-Access-Token"];
    
    NSData *responseDate = [NSURLConnection sendSynchronousRequest:request returningResponse:&response error:&error];
    
    if (error) {
        NSLog(@"Error getting Spots: %@",error);
    } else {
        NSArray *result = [NSJSONSerialization JSONObjectWithData:responseDate options:kNilOptions error:&error];
        
        NSMutableArray *spots = [NSMutableArray new];
        
        for (NSDictionary *first in result) {
            NSDictionary *locationDic = [first objectForKey:@"location"];
            SLocation *location = [[SLocation alloc]
                                   initWithId:[locationDic objectForKey:@"location_id"]
                                   latitude:[locationDic objectForKey:@"latitude"]
                                   andLongitude:[locationDic objectForKey:@"longitude"]];

            SSpot *spot = [[SSpot alloc]
                           initWithId:[first objectForKey:@"spot_id"]
                           name:[first objectForKey:@"name"]
                           radius:[first objectForKey:@"radius"]
                           andLocation:location];
            
            [spots addObject:spot];
        }
        return spots;
    }
    
    return nil;
}

-(void)differSpotsWithYelp{
    
    NSMutableArray *newYelpSpots = [NSMutableArray new];
    NSMutableArray *newOwnSpots = [NSMutableArray new];
    NSMutableArray *newMergedSpots = [NSMutableArray new];
    
    if (ourSpots.count > 0) {
        
        for (YLBusiness* b in yelpSpots) {

            BOOL isHybrid = false;
            
            NSDictionary *coordinate = [b.location objectForKey:@"coordinate"];
            NSString *lat1 = [NSString stringWithFormat:@"%.6f",[[coordinate objectForKey:@"latitude"] doubleValue]];
            NSString *lo1 = [NSString stringWithFormat:@"%.6f",[[coordinate objectForKey:@"longitude"] doubleValue]];
            
            for (SSpot *spot in ourSpots) {
    
                NSString *lat2 = [NSString stringWithFormat:@"%.6f",[spot.location.latitude doubleValue]];
                NSString *lo2 = [NSString stringWithFormat:@"%.6f",[spot.location.longitude doubleValue]];

                if ([lat1 isEqualToString:lat2] && [lo1 isEqualToString:lo2]) {
                    HybridSpot *hybridSpot = [[HybridSpot alloc] initWithYelpSpot:b andSSpot:spot];
                    [newMergedSpots addObject:hybridSpot];
                    isHybrid = true;
                    break;
                }
            }
            if (!isHybrid) {
                [newYelpSpots addObject:b];
            }
        }
        
    } else {
        [newYelpSpots addObjectsFromArray:yelpSpots];
    }
    
    for (SSpot *spot in ourSpots) {
        BOOL isHybrid = NO;
        for (HybridSpot *hy in newMergedSpots) {
            if ([hy.sspot.id isEqualToNumber:spot.id]) {
                isHybrid = YES;
                break;
            }
        }
        if (!isHybrid) {
            [newOwnSpots addObject:spot];
        }
    }
    
    yelpSpots = newYelpSpots;
    ourSpots = newOwnSpots;
    mergedSpots = newMergedSpots;
    
}

- (void)openSpotCreationView {
    
    YelpAddSpotViewController *vc = [YelpAddSpotViewController new];
    
    NSDictionary *address = [self getAddressForLocationWithLatitude:self.mapView.userLocation.coordinate.latitude andLongitude:self.mapView.userLocation.coordinate.longitude];
    
    vc.street = [address objectForKey:@"street"];
    vc.zipcode = [address objectForKey:@"zipcode"];
    vc.country = [address objectForKey:@"country"];
    
    CLLocation *location = [[CLLocation alloc] initWithLatitude:
                            self.mapView.userLocation.coordinate.latitude longitude:
                            self.mapView.userLocation.coordinate.longitude];
    
    vc.location = location;
    [self.navigationController pushViewController:vc animated:YES];
}

- (void)addSpotAtYourPosition {
    
    NSString *name = nameField.text;
    NSString *rangeString = range.text;
    
    [customView removeFromSuperview];
    customView = nil;
    
    NSDictionary *request_body = @{
                                   @"name" : name,
                                   @"radius" : rangeString,
                                   @"latitude" : [NSString stringWithFormat:@"%f",self.mapView.userLocation.coordinate.latitude],
                                   @"longitude" : [NSString stringWithFormat:@"%f",self.mapView.userLocation.coordinate.longitude]};
    
    
    //sending post request for auth in java backend
    NSString *urlString = [NSString stringWithFormat:@"%@/spot",BASEURL];
    
    NSError *error = nil;
    NSURLResponse *response = nil;
    
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] initWithURL:[NSURL URLWithString:urlString]];
    [request setHTTPMethod:@"POST"];
    [request setValue:delegate.token forHTTPHeaderField:@"X-Access-Token"];
    [request setValue:@"application/json" forHTTPHeaderField:@"Content-Type"];
    [request setHTTPBody:[NSJSONSerialization dataWithJSONObject:request_body options:NSJSONWritingPrettyPrinted error:&error]];
    
    if (error) {
        NSLog(@"Error setting HTTPBody: %@",error.localizedDescription);
    } else {
        NSData *responseData = [NSURLConnection sendSynchronousRequest:request returningResponse:&response error:&error];
        
        //testing
        NSHTTPURLResponse* httpResponse = (NSHTTPURLResponse*)response;
        long statuscode = [httpResponse statusCode];
        NSLog(@"Add Spot Statuscode: %ld",statuscode);
        
        if (error) {
            NSLog(@"Error creating Spot: %@",error);
        } else {
            if (statuscode == 201) {
                NSDictionary *result = [NSJSONSerialization JSONObjectWithData:responseData options:kNilOptions error:&error];
                if (error) {
                    NSLog(@"Error creating Spot NSJSONSerialization: %@",error.localizedDescription);
                } else {
                    NSLog(@"Spot successfully generated with id: %@",[result objectForKey:@"spot_id"]);
                }
            } else {
                NSLog(@"Statuscode: %ld error: %@",statuscode,[[NSString alloc] initWithData:responseData encoding:NSUTF8StringEncoding]);
            }
        }
    }
}

-(NSDictionary*)getAddressForLocationWithLatitude:(double)latitude andLongitude:(double)longitude {
    
    NSString *urlString = [NSString stringWithFormat:@"https://maps.googleapis.com/maps/api/geocode/json?latlng=%f,%f&key=AIzaSyC24gyjPO6QB-rdDdDIx8qeOcyrymQddCU",latitude,longitude];
    NSError *error = nil;
    NSURLResponse *response;
    
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] initWithURL:[NSURL URLWithString:urlString]];
    
    [request setHTTPMethod:@"GET"];
    
    NSData *responseData = [NSURLConnection sendSynchronousRequest:request returningResponse:&response error:&error];
    
    //testing
    NSHTTPURLResponse* httpResponse = (NSHTTPURLResponse*)response;
    long statuscode = [httpResponse statusCode];
    NSLog(@"Google api request Statuscode: %ld",statuscode);
    
    if (error) {
        NSLog(@"Google request failed: %@",error.localizedDescription);
        return nil;
    } else {
        
        NSString *street;
        NSString *streetNumber;
        NSString *zipcode;
        NSString *city;
        NSString *country;
        
        NSDictionary *result = [NSJSONSerialization JSONObjectWithData:responseData options:kNilOptions error:&error];
        NSArray *second = [result objectForKey:@"results"];
        NSDictionary *third = second[0];
        NSArray *fourth = [third objectForKey:@"address_components"];
        NSDictionary *fifth = fourth[1];
        street = [fifth objectForKey:@"short_name"];
        NSDictionary *sixth = fourth[0];
        streetNumber = [sixth objectForKey:@"short_name"];
        NSDictionary *seventh = fourth[fourth.count-1];
        zipcode = [seventh objectForKey:@"short_name"];
        NSDictionary *eighth = fourth[3];
        city = [eighth objectForKey:@"short_name"];
        NSDictionary *ninth = fourth[5];
        country = [ninth objectForKey:@"long_name"];
        
        return @{@"street":[NSString stringWithFormat:@"%@ %@",street,streetNumber],
                 @"zipcode":[NSString stringWithFormat:@"%@ %@",zipcode, city],
                 @"country":country};
    }
}

#pragma mark - UITextField delegate

-(BOOL)textFieldShouldReturn:(UITextField *)textField {
    [textField resignFirstResponder];
    return true;
}

-(void)dismissKeyboard {
    [range resignFirstResponder];
    [nameField resignFirstResponder];
}

@end
