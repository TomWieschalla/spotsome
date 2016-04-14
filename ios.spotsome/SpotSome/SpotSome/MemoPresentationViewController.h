//
//  MemoPresentationViewController.h
//  SpotSome
//
//  Created by Tom Wieschalla  on 28.07.15.
//  Copyright (c) 2015 SpotSome@Beuth. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <AVFoundation/AVFoundation.h>

@interface MemoPresentationViewController : UIViewController <AVAudioPlayerDelegate>

@property (strong, nonatomic) NSData *memoData;

@property (weak, nonatomic) IBOutlet UIButton *playButton;
- (IBAction)play:(id)sender;
- (IBAction)share:(id)sender;

@end
