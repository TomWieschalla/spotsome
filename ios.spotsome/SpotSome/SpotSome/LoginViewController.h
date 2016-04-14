//
//  LoginViewController.h
//  SpotSome
//
//  Created by Tom Wieschalla  on 22.06.15.
//  Copyright (c) 2015 SpotSome@Beuth. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <Twitter/Twitter.h>

@interface LoginViewController : UIViewController <UITextFieldDelegate>
@property (weak, nonatomic) IBOutlet UIImageView *twitterImage;

@end
