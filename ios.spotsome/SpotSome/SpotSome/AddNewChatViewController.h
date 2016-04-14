//
//  AddNewChatViewController.h
//  SpotSome
//
//  Created by Tom Wieschalla  on 29.07.15.
//  Copyright (c) 2015 SpotSome@Beuth. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "SSpot.h"

@interface AddNewChatViewController : UIViewController <UITextViewDelegate>

@property (strong, nonatomic) SSpot *spot;

@property (weak, nonatomic) IBOutlet UITextView *titelTextView;
@property (weak, nonatomic) IBOutlet UIButton *addChatButton;
- (IBAction)addChat:(id)sender;

@end
