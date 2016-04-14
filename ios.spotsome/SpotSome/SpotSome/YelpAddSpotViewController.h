//
//  YelpAddSpotViewController.h
//  SpotSome
//
//  Created by Tom Wieschalla  on 25.07.15.
//  Copyright (c) 2015 SpotSome@Beuth. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <CoreLocation/CoreLocation.h>

@interface YelpAddSpotViewController : UIViewController <UITextFieldDelegate, UIPickerViewDataSource, UIPickerViewDelegate>

@property (strong, nonatomic) NSString *street;
@property (strong, nonatomic) NSString *zipcode;
@property (strong, nonatomic) NSString *country;

@property (strong, nonatomic) CLLocation *location;

@property (weak, nonatomic) IBOutlet UILabel *countryLabel;
@property (weak, nonatomic) IBOutlet UILabel *zipcodeLabel;
@property (weak, nonatomic) IBOutlet UILabel *streetLabel;
@property (weak, nonatomic) IBOutlet UITextField *nameTextField;
@property (weak, nonatomic) IBOutlet UIPickerView *rangePicker;

- (IBAction)addSpot:(id)sender;

@end
