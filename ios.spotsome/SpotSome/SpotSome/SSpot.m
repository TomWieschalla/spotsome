//
//  SSpot.m
//  SpotSome
//
//  Created by Tom Wieschalla  on 08.07.15.
//  Copyright (c) 2015 SpotSome@Beuth. All rights reserved.
//

#import "SSpot.h"
#import "SLocation.h"


@implementation SSpot

-(id)initWithId:(NSNumber *)ID name:(NSString *)name radius:(NSNumber *)radius andLocation:(SLocation *)location {
    
    self = [super init];
    
    if (self) {
        self.id = ID;
        self.name = name;
        self.radius = radius;
        self.location = location;
    }
    return self;
}

@end
