//
//  MessagePresentationViewController.h
//  SpotSome
//
//  Created by Tom Wieschalla  on 28.07.15.
//  Copyright (c) 2015 SpotSome@Beuth. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface MessagePresentationViewController : UIViewController

@property (strong, nonatomic) NSString *message;

@property (weak, nonatomic) IBOutlet UITextView *messageTextView;
@property (weak, nonatomic) IBOutlet UIButton *shareButton;

- (IBAction)share:(id)sender;


@end
