//
//  SBoard.h
//  SpotSome
//
//  Created by Tom Wieschalla  on 27.07.15.
//  Copyright (c) 2015 SpotSome@Beuth. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "SSpot.h"

@interface SBoard : NSObject

@property (strong, nonatomic) NSString *name;
@property (strong, nonatomic) SSpot *spot;
@property (strong, nonatomic) NSArray *messages;
@property (strong, nonatomic) NSNumber *boardID;

-(id)initWithName:(NSString*)name spot:(SSpot*)spot messages:(NSArray*)messages andBoardID:(NSNumber*)boardID;

@end
