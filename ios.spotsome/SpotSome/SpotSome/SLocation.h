//
//  SLocation.h
//  SpotSome
//
//  Created by Tom Wieschalla  on 08.07.15.
//  Copyright (c) 2015 SpotSome@Beuth. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreData/CoreData.h>


@interface SLocation : NSObject

@property (nonatomic, retain) NSNumber * id;
@property (nonatomic, retain) NSNumber * latitude;
@property (nonatomic, retain) NSNumber * longitude;

-(id)initWithId:(NSNumber *)ID latitude:(NSNumber *)latitude andLongitude:(NSNumber *)longitude;

@end
