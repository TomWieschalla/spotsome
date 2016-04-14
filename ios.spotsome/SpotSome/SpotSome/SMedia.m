//
//  SMedia.m
//  SpotSome
//
//  Created by Tom Wieschalla  on 28.07.15.
//  Copyright (c) 2015 SpotSome@Beuth. All rights reserved.
//

#import "SMedia.h"

@implementation SMedia

-(id)initWithMessageID:(NSNumber *)messageID messageText:(NSString *)messageText type:(NSString *)mime_type mediaID:(NSNumber *)mediaID date:(NSDate *)date andFileName:(NSString *)fileName{
    self = [super init];
    
    if (self) {
        self.messageID = messageID;
        self.messageText = messageText;
        self.mime_type = mime_type;
        self.mediaID = mediaID;
        self.date = date;
        self.fileName = fileName;
    }
    
    return self;
}
@end
