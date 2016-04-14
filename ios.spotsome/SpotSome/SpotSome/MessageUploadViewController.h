//
//  MessageUploadViewController.h
//  SpotSome
//
//  Created by Tom Wieschalla  on 28.07.15.
//  Copyright (c) 2015 SpotSome@Beuth. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "SSpot.h"
#import "SBoard.h"

@interface MessageUploadViewController : UIViewController <UITextFieldDelegate, UITextViewDelegate>

@property (strong, nonatomic) SSpot *spot;
@property (strong, nonatomic) SBoard *board;
@property (weak, nonatomic) IBOutlet UITextView *textView;
@property (weak, nonatomic) IBOutlet UIButton *uploadButton;
- (IBAction)uploadMessage:(id)sender;

@end
