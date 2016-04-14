//
//  SMedia.h
//  SpotSome
//
//  Created by Tom Wieschalla  on 28.07.15.
//  Copyright (c) 2015 SpotSome@Beuth. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface SMedia : NSObject

@property (strong, nonatomic) NSNumber *messageID;
@property (strong, nonatomic) NSString *messageText;
@property (strong, nonatomic) NSString *mime_type;
@property (strong, nonatomic) NSNumber *mediaID;
@property (strong, nonatomic) NSDate *date;
@property (strong, nonatomic) NSString *fileName;

-(id)initWithMessageID:(NSNumber *)messageID messageText:(NSString *)messageText type:(NSString *)mime_type mediaID:(NSNumber *)mediaID date:(NSDate *)date andFileName:(NSString*)fileName;

@end
