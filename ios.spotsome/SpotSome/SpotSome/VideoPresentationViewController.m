//
//  VideoPresentationViewController.m
//  SpotSome
//
//  Created by Tom Wieschalla  on 28.07.15.
//  Copyright (c) 2015 SpotSome@Beuth. All rights reserved.
//

#import "VideoPresentationViewController.h"
#import <AVFoundation/AVFoundation.h>
#import <Social/Social.h>

@implementation VideoPresentationViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    if ([self.type isEqualToString:@"picture"]) {
        UIImage *image = [UIImage imageWithData:self.pictureData];
        [self.presentationImageView setImage:image];
        self.title = @"Foto";
    } else {
        [self.presentationImageView setImage:[self generateThumbImage:self.videoPath]];
        UITapGestureRecognizer *movieTap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(showMovie)];
        [self.presentationImageView addGestureRecognizer:movieTap];
        self.presentationImageView.userInteractionEnabled = YES;
        self.title = @"Video";
    }

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
    if ([SLComposeViewController isAvailableForServiceType:SLServiceTypeTwitter])
    {
        SLComposeViewController *tweetSheet = [SLComposeViewController
                                               composeViewControllerForServiceType:SLServiceTypeTwitter];
        if ([self.type isEqualToString:@"picture"]) {
            UIImage *image = [UIImage imageWithData:self.pictureData];
            [tweetSheet addImage:image];
            [tweetSheet setInitialText:@"#Spotsome Cooles Foto!"];
        } else {
            UIImage *image = [self generateThumbImage:self.videoPath];
            [tweetSheet addImage:image];
            [tweetSheet setInitialText:@"#Spotsome Cooles Video!"];
        }
        
        [self presentViewController:tweetSheet animated:YES completion:nil];
    }
}

-(UIImage *)generateThumbImage:(NSString *)filepath {
    NSURL *url = [NSURL fileURLWithPath:filepath];
    
    AVAsset *asset = [AVAsset assetWithURL:url];
    AVAssetImageGenerator *imageGenerator = [[AVAssetImageGenerator alloc]initWithAsset:asset];
    CMTime time = [asset duration];
    time.value = 0;
    CGImageRef imageRef = [imageGenerator copyCGImageAtTime:time actualTime:NULL error:NULL];
    UIImage *thumbnail = [UIImage imageWithCGImage:imageRef];
    CGImageRelease(imageRef);  // CGImageRef won't be released by ARC
    
    return thumbnail;
}

-(void)showMovie {
    
    if (self.videoPath) {
        MPMoviePlayerViewController *theMovie = [[MPMoviePlayerViewController alloc]
                                                 initWithContentURL:[NSURL fileURLWithPath:self.videoPath]];
        
        [self presentMoviePlayerViewControllerAnimated:theMovie];
    }
    
}

@end
