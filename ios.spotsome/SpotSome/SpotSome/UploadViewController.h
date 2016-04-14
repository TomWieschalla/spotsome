//
//  UploadViewController.h
//  SpotSome
//
//  Created by Tom Wieschalla  on 28.07.15.
//  Copyright (c) 2015 SpotSome@Beuth. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "SSpot.h"
#import "SBoard.h"

@interface UploadViewController : UIViewController

@property (strong, nonatomic) SSpot *spot;
@property (strong, nonatomic) SBoard *board;

@property (weak, nonatomic) IBOutlet UIButton *videoButton;
@property (weak, nonatomic) IBOutlet UIButton *memoButton;
@property (weak, nonatomic) IBOutlet UIButton *messageButton;
- (IBAction)openVideoUploadView:(id)sender;
- (IBAction)openMemoUploadView:(id)sender;
- (IBAction)openMessageUploadView:(id)sender;

@end
