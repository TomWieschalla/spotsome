//
//  MessagesViewController.h
//  SpotSome
//
//  Created by Tom Wieschalla  on 29.07.15.
//  Copyright (c) 2015 SpotSome@Beuth. All rights reserved.
//

#import <UIKit/UIKit.h>

#import "JSQMessage.h"
#import "JSQMessages.h"
#import "SChat.h"

@class MessagesViewController;

@protocol JSQDemoViewControllerDelegate <NSObject>

- (void)didDismissJSQDemoViewController:(MessagesViewController *)vc;

@end



@interface MessagesViewController : JSQMessagesViewController <UIActionSheetDelegate, NSURLConnectionDelegate>

@property (weak, nonatomic) id<JSQDemoViewControllerDelegate> delegateModal;

@property (strong, nonatomic) NSMutableArray *chatData;

@property (strong, nonatomic) SChat *chat;


@end
