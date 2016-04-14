//
//  HybridSpot.h
//  SpotSome
//
//  Created by Tom Wieschalla  on 08.07.15.
//  Copyright (c) 2015 SpotSome@Beuth. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "SSpot.h"
#import "YLBusiness.h"

@interface HybridSpot : NSObject

@property (strong, nonatomic) SSpot *sspot;
@property (strong, nonatomic) YLBusiness *yelpSpot;

-(id)initWithYelpSpot:(YLBusiness*)yelpSpot andSSpot:(SSpot *)sspot;


@end
