//
//  SChat.m
//  SpotSome
//
//  Created by Tom Wieschalla  on 27.07.15.
//  Copyright (c) 2015 SpotSome@Beuth. All rights reserved.
//

#import "SChat.h"

@implementation SChat

-(id)initWithName:(NSString*)name spot:(SSpot*)spot messages:(NSMutableArray*)messages andChatID:(NSNumber*)chatID {
    
    self = [super init];
    
    if (self) {
        self.name = name;
        self.spot = spot;
        self.messages = messages;
        self.chatID = chatID;
    }
    
    return self;
}

@end
