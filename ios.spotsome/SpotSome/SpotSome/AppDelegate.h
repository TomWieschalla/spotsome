//
//  AppDelegate.h
//  SpotSome
//
//  Created by Tom Wieschalla  on 11.05.15.
//  Copyright (c) 2015 SpotSome@Beuth. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <CoreData/CoreData.h>

@interface AppDelegate : UIResponder <UIApplicationDelegate>

@property (strong, nonatomic) UIWindow *window;

@property (readonly, strong, nonatomic) NSManagedObjectContext *managedObjectContext;
@property (readonly, strong, nonatomic) NSManagedObjectModel *managedObjectModel;
@property (readonly, strong, nonatomic) NSPersistentStoreCoordinator *persistentStoreCoordinator;
@property (strong, nonatomic) NSString *token;
@property (strong, nonatomic) NSString *twitterName;
@property (strong, nonatomic) NSNumber *userID;
@property (strong, nonatomic) NSString *deviceToken;

- (void)saveContext;
- (NSURL *)applicationDocumentsDirectory;


@end

