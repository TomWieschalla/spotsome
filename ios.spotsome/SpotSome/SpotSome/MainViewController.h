//
//  MainViewController.h
//  SpotSome
//
//  Created by Tom Wieschalla  on 22.06.15.
//  Copyright (c) 2015 SpotSome@Beuth. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <MapKit/MapKit.h>

@interface MainViewController : UIViewController <MKMapViewDelegate, CLLocationManagerDelegate, UIPopoverPresentationControllerDelegate,UITextFieldDelegate>

@property (strong, nonatomic) IBOutlet MKMapView *mapView;
@property (strong, nonatomic) IBOutlet UIButton *refreshButton;
@property (strong, nonatomic) CLLocationManager *manager;
@property (nonatomic, strong) UIPopoverController *userDataPopover;

- (IBAction)refresh:(id)sender;

@end
