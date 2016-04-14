//
//  SSpot.h
//  SpotSome
//
//  Created by Tom Wieschalla  on 08.07.15.
//  Copyright (c) 2015 SpotSome@Beuth. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreData/CoreData.h>

@class SLocation;

@interface SSpot : NSObject

@property (nonatomic, retain) NSNumber * id;
@property (nonatomic, retain) NSString * name;
@property (nonatomic, retain) NSNumber * radius;
@property (nonatomic, retain) SLocation *location;

-(id)initWithId:(NSNumber *)ID name:(NSString *)name radius:(NSNumber *)radius andLocation:(SLocation *)location;

@end
