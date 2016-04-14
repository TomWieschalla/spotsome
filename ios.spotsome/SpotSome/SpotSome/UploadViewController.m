//
//  UploadViewController.m
//  SpotSome
//
//  Created by Tom Wieschalla  on 28.07.15.
//  Copyright (c) 2015 SpotSome@Beuth. All rights reserved.
//

#import "UploadViewController.h"
#import "VideoUploadViewController.h"
#import "MemoUploadViewController.h"
#import "MessageUploadViewController.h"

@implementation UploadViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.title = @"Upload";
    
    //customize buttons
    [self.videoButton.layer setBorderWidth:2];
    [self.videoButton.layer setBorderColor:[[UIColor clearColor] CGColor]];
    [self.videoButton.layer setCornerRadius:20];
    
    [self.memoButton.layer setBorderWidth:2];
    [self.memoButton.layer setBorderColor:[[UIColor clearColor] CGColor]];
    [self.memoButton.layer setCornerRadius:20];
    
    [self.messageButton.layer setBorderWidth:2];
    [self.messageButton.layer setBorderColor:[[UIColor clearColor] CGColor]];
    [self.messageButton.layer setCornerRadius:20];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)openVideoUploadView:(id)sender {
    VideoUploadViewController *vc = [VideoUploadViewController new];
    
    vc.spot = self.spot;
    vc.board = self.board;
    [self.navigationController pushViewController:vc animated:YES];
}

- (IBAction)openMemoUploadView:(id)sender {
    MemoUploadViewController *vc = [MemoUploadViewController new];
    
    vc.spot = self.spot;
    vc.board = self.board;
    [self.navigationController pushViewController:vc animated:YES];
}

- (IBAction)openMessageUploadView:(id)sender {
    MessageUploadViewController *vc = [MessageUploadViewController new];
    
    vc.spot = self.spot;
    vc.board = self.board;
    [self.navigationController pushViewController:vc animated:YES];
}
@end
