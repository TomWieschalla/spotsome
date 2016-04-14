//
//  MemoUploadViewController.h
//  SpotSome
//
//  Created by Tom Wieschalla  on 28.07.15.
//  Copyright (c) 2015 SpotSome@Beuth. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "SSpot.h"
#import "SBoard.h"
#import <AVFoundation/AVFoundation.h>

@interface MemoUploadViewController : UIViewController <AVAudioRecorderDelegate, AVAudioPlayerDelegate, UITextFieldDelegate>

@property (strong, nonatomic) SSpot *spot;
@property (strong, nonatomic) SBoard *board;
@property (weak, nonatomic) IBOutlet UIButton *statusButton;

@property (weak, nonatomic) IBOutlet UIButton *startButton;
- (IBAction)startStopRecording:(id)sender;
@property (weak, nonatomic) IBOutlet UIButton *uploadButton;
- (IBAction)uploadMemo:(id)sender;
- (IBAction)playMemo:(id)sender;
@property (weak, nonatomic) IBOutlet UITextField *descriptiontextField;

@end
