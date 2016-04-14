//
//  MessagePresentationViewController.m
//  SpotSome
//
//  Created by Tom Wieschalla  on 28.07.15.
//  Copyright (c) 2015 SpotSome@Beuth. All rights reserved.
//

#import "MessagePresentationViewController.h"
#import <Social/Social.h>

@implementation MessagePresentationViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    self.messageTextView.text = self.message;
    
    self.title = @"Nachricht";
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

- (IBAction)share:(id)sender {
    SLComposeViewController *tweetSheet = [SLComposeViewController
                                           composeViewControllerForServiceType:SLServiceTypeTwitter];

    [tweetSheet setInitialText:@"#Spotsome Cooler Spot!"];

    [self presentViewController:tweetSheet animated:YES completion:nil];
}

@end
