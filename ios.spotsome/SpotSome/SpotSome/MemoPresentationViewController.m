//
//  MemoPresentationViewController.m
//  SpotSome
//
//  Created by Tom Wieschalla  on 28.07.15.
//  Copyright (c) 2015 SpotSome@Beuth. All rights reserved.
//

#import "MemoPresentationViewController.h"
#import <Social/Social.h>

@implementation MemoPresentationViewController {
    AVAudioPlayer *player;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    self.title = @"Audio";
    
    [self.playButton.layer setBorderWidth:2];
    [self.playButton.layer setBorderColor:[[UIColor clearColor] CGColor]];
    [self.playButton setBackgroundColor:[UIColor blueColor]];
    [self.playButton.layer setCornerRadius:40];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void) audioPlayerDidFinishPlaying:(AVAudioPlayer *)player successfully:(BOOL)flag{
    UIAlertView *alert = [[UIAlertView alloc] initWithTitle: @"Done"
                                                    message: @"Finish playing the recording!"
                                                   delegate: nil
                                          cancelButtonTitle:@"OK"
                                          otherButtonTitles:nil];
    [alert show];
    [self.playButton setBackgroundColor:[UIColor blueColor]];
    [self.playButton.layer setCornerRadius:40];
    [self.playButton setTitle:@"Play" forState:UIControlStateNormal];
}

- (IBAction)play:(id)sender {
    NSError *error = nil;
    player = [[AVAudioPlayer alloc] initWithData:self.memoData error:&error];
    player.delegate = self;
    [self.playButton setBackgroundColor:[UIColor redColor]];
    [self.playButton.layer setCornerRadius:20];
    [self.playButton setTitle:@"..." forState:UIControlStateNormal];
    [player play];
}

- (IBAction)share:(id)sender {
    
    SLComposeViewController *tweetSheet = [SLComposeViewController
                                           composeViewControllerForServiceType:SLServiceTypeTwitter];
    
    [tweetSheet setInitialText:@"#Spotsome Cooler Spot!"];
    
    [self presentViewController:tweetSheet animated:YES completion:nil];
}
@end
