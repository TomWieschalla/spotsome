//
//  SChat.h
//  SpotSome
//
//  Created by Tom Wieschalla  on 27.07.15.
//  Copyright (c) 2015 SpotSome@Beuth. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "SSpot.h"

@interface SChat : NSObject

@property (strong, nonatomic) NSString *name;
@property (strong, nonatomic) SSpot *spot;
@property (strong, nonatomic) NSMutableArray *messages;
@property (strong, nonatomic) NSNumber *chatID;

-(id)initWithName:(NSString*)name spot:(SSpot*)spot messages:(NSArray*)messages andChatID:(NSNumber*)chatID;

@end
