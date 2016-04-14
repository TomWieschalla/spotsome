//
//  SpotsomeSpotMainViewController.h
//  SpotSome
//
//  Created by Tom Wieschalla  on 08.07.15.
//  Copyright (c) 2015 SpotSome@Beuth. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "SSpot.h"

@interface SpotsomeSpotMainViewController : UITableViewController <UITableViewDelegate>

@property (strong, nonatomic) SSpot *spot;

@property (strong, nonatomic) NSString *street;
@property (strong, nonatomic) NSString *zipcode;
@property (strong, nonatomic) NSString *country;

@end
