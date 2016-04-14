//
//  SpotPushTableViewController.h
//  SpotSome
//
//  Created by Tom Wieschalla  on 07.07.15.
//  Copyright (c) 2015 SpotSome@Beuth. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "YLBusiness.h"
#import "SSpot.h"

@interface YelpTableViewController : UITableViewController <UITableViewDelegate>
@property (weak, nonatomic) IBOutlet UIImageView *imageView;
@property (nonatomic) BOOL isHybrid;
@property (strong, nonatomic) SSpot *spot;

@property (strong, nonatomic) YLBusiness* tappedBusiness;

@end
