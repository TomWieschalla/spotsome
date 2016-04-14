//
//  VideoPresentationViewController.h
//  SpotSome
//
//  Created by Tom Wieschalla  on 28.07.15.
//  Copyright (c) 2015 SpotSome@Beuth. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <MediaPlayer/MediaPlayer.h>
#import <AssetsLibrary/AssetsLibrary.h>
#import <MobileCoreServices/MobileCoreServices.h>

@interface VideoPresentationViewController : UIViewController <UINavigationControllerDelegate>

@property (strong, nonatomic) NSData *pictureData;
@property (strong, nonatomic) NSString *type;
@property (strong, nonatomic) NSString *videoPath;

@property (weak, nonatomic) IBOutlet UIImageView *presentationImageView;
@property (weak, nonatomic) IBOutlet UIButton *shareButton;

- (IBAction)share:(id)sender;



@end
