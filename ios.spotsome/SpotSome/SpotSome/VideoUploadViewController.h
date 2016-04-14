//
//  VideoUploadViewController.h
//  SpotSome
//
//  Created by Tom Wieschalla  on 28.07.15.
//  Copyright (c) 2015 SpotSome@Beuth. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "SSpot.h"
#import "SBoard.h"
#import <MediaPlayer/MediaPlayer.h>
#import <AssetsLibrary/AssetsLibrary.h>
#import <MobileCoreServices/MobileCoreServices.h>

@interface VideoUploadViewController : UIViewController <UIImagePickerControllerDelegate, UINavigationControllerDelegate, UITextFieldDelegate>

@property (strong, nonatomic) SSpot *spot;
@property (strong, nonatomic) SBoard *board;
@property (weak, nonatomic) IBOutlet UIImageView *imageView;
- (IBAction)createVideo:(id)sender;
- (IBAction)getVideo:(id)sender;
- (IBAction)uploadToBoard:(id)sender;
@property (weak, nonatomic) IBOutlet UIButton *makeButton;
@property (weak, nonatomic) IBOutlet UIButton *takeButton;
@property (weak, nonatomic) IBOutlet UIButton *uploadButton;
@property (weak, nonatomic) IBOutlet UITextField *descriptionTextField;

@end
