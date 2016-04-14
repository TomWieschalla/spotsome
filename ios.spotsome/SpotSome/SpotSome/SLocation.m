//
//  SLocation.m
//  SpotSome
//
//  Created by Tom Wieschalla  on 08.07.15.
//  Copyright (c) 2015 SpotSome@Beuth. All rights reserved.
//

#import "SLocation.h"


@implementation SLocation

-(id)initWithId:(NSNumber *)ID latitude:(NSNumber *)latitude andLongitude:(NSNumber *)longitude {
    
    self = [super init];
    
    if (self) {
        self.id = ID;
        self.latitude = latitude;
        self.longitude = longitude;
    }
    return self;
}

@end
