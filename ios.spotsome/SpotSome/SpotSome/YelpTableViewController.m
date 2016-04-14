//
//  SpotPushTableViewController.m
//  SpotSome
//
//  Created by Tom Wieschalla  on 07.07.15.
//  Copyright (c) 2015 SpotSome@Beuth. All rights reserved.
//

#import "YelpTableViewController.h"
#import "YelpAddressTableViewCell.h"
#import "YelpIdTableViewCell.h"
#import "PushTableViewCell.h"
#import "YelpAddSpotViewController.h"
#import "SpotsomeSpotMainViewController.h"

@implementation YelpTableViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    self.tableView.tableFooterView = [[UIView alloc] initWithFrame:CGRectZero];
    
    [self.tableView registerNib:[UINib nibWithNibName:@"YelpIdTableViewCell" bundle:nil] forCellReuseIdentifier:@"first"];
    [self.tableView registerNib:[UINib nibWithNibName:@"YelpAddressTableViewCell" bundle:nil] forCellReuseIdentifier:@"second"];
    [self.tableView registerNib:[UINib nibWithNibName:@"PushTableViewCell" bundle:nil] forCellReuseIdentifier:@"third"];
    [self.tableView registerNib:[UINib nibWithNibName:@"PushTableViewCell" bundle:nil] forCellReuseIdentifier:@"fourth"];
    
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark - Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {

    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {

    return 4;
}

-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    CGFloat height = 0;
    
    if (indexPath.row == 0) {
        
        height = 75;
        
    } else if (indexPath.row == 1){
        
        height = 135;
        
    } else {
        
        height = 50;
        
    }
    
    return height;
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    UITableViewCell *cell = nil;
    
    if (indexPath.row == 0) {
        YelpIdTableViewCell *yelpCell = [tableView dequeueReusableCellWithIdentifier:@"first"];
        yelpCell.websiteLabel.text = self.tappedBusiness.id;
        cell = yelpCell;
    } else if (indexPath.row == 1){
        YelpAddressTableViewCell *yelpCell = (YelpAddressTableViewCell *)[tableView dequeueReusableCellWithIdentifier:@"second"];
        yelpCell.streetLabel.text = [self.tappedBusiness.location objectForKey:@"display_address"][0];
        yelpCell.zipcodeLabel.text = [self.tappedBusiness.location objectForKey:@"display_address"][2];
        yelpCell.countryLabel.text = [self.tappedBusiness.location objectForKey:@"display_address"][3];
        cell = yelpCell;
    } else if (indexPath.row == 2){
        PushTableViewCell *pushCell = [tableView dequeueReusableCellWithIdentifier:@"third"];
        pushCell.textLabel.text = @"Zur Yelp-App";
        cell = pushCell;
    } else {
        PushTableViewCell *pushCell = [tableView dequeueReusableCellWithIdentifier:@"fourth"];
        [pushCell.imageView setImage:[UIImage imageNamed:@"Plus Filled-100"]];
        
        if (self.isHybrid) {
            pushCell.textLabel.text = @"Zum Spot";
        } else {
            pushCell.textLabel.text = @"Spot erstellen";
        }
        cell = pushCell;
    }
    
    return cell;
}

-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    
    if (indexPath.row == 2) {
        if ([[UIApplication sharedApplication] canOpenURL:[NSURL URLWithString:@"yelp:"]]) {
            NSString *yelpSegueURI = [NSString stringWithFormat:@"yelp:///search?cll=%@,%@",[self.tappedBusiness.location objectForKey:@"latitude"],[self.tappedBusiness.location objectForKey:@"latitude"]];
            [[UIApplication sharedApplication]
             openURL:[NSURL URLWithString:self.tappedBusiness.url]];
        }
    } else if (indexPath.row == 3) {
        if (self.isHybrid) {
            
            UIStoryboard *sb = [UIStoryboard storyboardWithName:@"Main" bundle:nil];
            SpotsomeSpotMainViewController *vc = (SpotsomeSpotMainViewController*)[sb instantiateViewControllerWithIdentifier:@"spotmain"];
            
            vc.spot = self.spot;
            vc.street = [self.tappedBusiness.location objectForKey:@"display_address"][0];
            vc.zipcode = [self.tappedBusiness.location objectForKey:@"display_address"][2];
            vc.country = [self.tappedBusiness.location objectForKey:@"display_address"][3];
            
            vc.spot = self.spot;
            [self.navigationController pushViewController:vc animated:YES];
        } else {
            YelpAddSpotViewController *vc = [YelpAddSpotViewController new];
            vc.street = [self.tappedBusiness.location objectForKey:@"display_address"][0];
            vc.zipcode = [self.tappedBusiness.location objectForKey:@"display_address"][2];
            vc.country = [self.tappedBusiness.location objectForKey:@"display_address"][3];

            CLLocation *location = [[CLLocation alloc] initWithLatitude:
                                    [[[self.tappedBusiness.location objectForKey:@"coordinate"] objectForKey:@"latitude"] doubleValue] longitude:
                                    [[[self.tappedBusiness.location objectForKey:@"coordinate"] objectForKey:@"longitude"] doubleValue]];
            
            vc.location = location;
            [self.navigationController pushViewController:vc animated:YES];
        }
    }
}

@end
