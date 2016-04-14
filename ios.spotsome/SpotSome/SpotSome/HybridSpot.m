//
//  HybridSpot.m
//  SpotSome
//
//  Created by Tom Wieschalla  on 08.07.15.
//  Copyright (c) 2015 SpotSome@Beuth. All rights reserved.
//

#import "HybridSpot.h"

@implementation HybridSpot

-(id)initWithYelpSpot:(YLBusiness*)yelpSpot andSSpot:(SSpot *)sspot {
    self = [super init];
    
    if (self) {
        self.sspot = sspot;
        self.yelpSpot = yelpSpot;
    }
    return self;
}

@end
