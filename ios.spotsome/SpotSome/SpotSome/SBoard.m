//
//  SBoard.m
//  SpotSome
//
//  Created by Tom Wieschalla  on 27.07.15.
//  Copyright (c) 2015 SpotSome@Beuth. All rights reserved.
//

#import "SBoard.h"

@implementation SBoard

-(id)initWithName:(NSString*)name spot:(SSpot*)spot messages:(NSArray*)messages andBoardID:(NSNumber*)boardID {
    
    self = [super init];
    
    if (self) {
        self.name = name;
        self.spot = spot;
        self.messages = messages;
        self.boardID = boardID;
    }
    
    return self;
}

@end
