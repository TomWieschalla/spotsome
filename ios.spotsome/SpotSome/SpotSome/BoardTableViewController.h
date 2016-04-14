//
//  BoardTableViewController.h
//  SpotSome
//
//  Created by Tom Wieschalla  on 08.07.15.
//  Copyright (c) 2015 SpotSome@Beuth. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "SSpot.h"
#import "SBoard.h"

@interface BoardTableViewController : UITableViewController

@property (strong, nonatomic) SSpot *spot;
@property (strong, nonatomic) SBoard *board;

@end
