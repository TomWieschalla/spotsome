//
//  YelpAddSpotViewController.m
//  SpotSome
//
//  Created by Tom Wieschalla  on 25.07.15.
//  Copyright (c) 2015 SpotSome@Beuth. All rights reserved.
//

#import "YelpAddSpotViewController.h"
#import "AppConstants.h"
#import "AppDelegate.h"
#import "MainViewController.h"

@implementation YelpAddSpotViewController{
    NSArray *pickerRange;
    NSArray *pickerValues;
    NSString *currentRange;
    AppDelegate *delegate;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    
    delegate = [[UIApplication sharedApplication] delegate];
    
    UITapGestureRecognizer *tap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(dismissKeyboard)];
    [self.view addGestureRecognizer:tap];
    
    //textfield delegate
    self.nameTextField.delegate = self;
    
    //picker source and delegate
    self.rangePicker.delegate = self;
    self.rangePicker.dataSource = self;

    pickerRange = @[@"200 m",@"250 m",@"300 m",@"350 m",@"400 m",@"450 m"];
    pickerValues = @[@"200",@"250",@"300",@"350",@"400",@"450"];
    
    self.streetLabel.text = self.street;
    self.zipcodeLabel.text = self.zipcode;
    self.countryLabel.text = self.country;
    
    [self.rangePicker selectRow:4 inComponent:0 animated:YES];
    currentRange = @"300";
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


- (IBAction)addSpot:(id)sender {
    
    NSString *name = self.nameTextField.text;
    NSString *rangeString = currentRange;

    NSLog(@"%f,%f",self.location.coordinate.latitude,self.location.coordinate.longitude);
    NSString *lat = [NSString stringWithFormat:@"%.6f",self.location.coordinate.latitude];
    NSString *lon = [NSString stringWithFormat:@"%.6f",self.location.coordinate.longitude];
    
    NSDictionary *request_body = @{
                                   @"name" : name,
                                   @"radius" : rangeString,
                                   @"latitude" : lat,
                                   @"longitude" : lon};
    
    
    //sending post request for auth in java backend
    NSString *urlString = [NSString stringWithFormat:@"%@/spot",BASEURL];
    
    NSError *error = nil;
    NSURLResponse *response = nil;
    
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] initWithURL:[NSURL URLWithString:urlString]];
    [request setHTTPMethod:@"POST"];
    [request setValue:delegate.token forHTTPHeaderField:@"X-Access-Token"];
    [request setValue:@"application/json" forHTTPHeaderField:@"Content-Type"];
    [request setHTTPBody:[NSJSONSerialization dataWithJSONObject:request_body options:NSJSONWritingPrettyPrinted error:&error]];
    
    if (error) {
        NSLog(@"Error setting HTTPBody: %@",error.localizedDescription);
    } else {
        NSData *responseData = [NSURLConnection sendSynchronousRequest:request returningResponse:&response error:&error];
        
        //testing
        NSHTTPURLResponse* httpResponse = (NSHTTPURLResponse*)response;
        long statuscode = [httpResponse statusCode];
        NSLog(@"Add Spot Statuscode: %ld",statuscode);
        
        if (error) {
            NSLog(@"Error creating Spot: %@",error);
        } else {
            if (statuscode == 201) {
                NSDictionary *result = [NSJSONSerialization JSONObjectWithData:responseData options:kNilOptions error:&error];
                if (error) {
                    NSLog(@"Error creating Spot NSJSONSerialization: %@",error.localizedDescription);
                } else {
                    NSLog(@"Spot successfully generated with id: %@",[result objectForKey:@"spot_id"]);
                    MainViewController *mv = [[MainViewController alloc] initWithNibName:@"MainViewController" bundle:nil];
                    UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Erfolgreich" message:@"Spot erfolgreich erstellt!" delegate:self cancelButtonTitle:@"Ok" otherButtonTitles:nil];
                    [alert show];
                    UINavigationController *nav = [[UINavigationController alloc] initWithRootViewController:mv];
                    [self.navigationController presentViewController:nav animated:YES completion:nil];
                }
            } else {
                NSLog(@"Statuscode: %ld error: %@",statuscode,[[NSString alloc] initWithData:responseData encoding:NSUTF8StringEncoding]);
            }
        }
    }
}

#pragma mark - UITextField delegate

-(BOOL)textFieldShouldReturn:(UITextField *)textField {
    [textField resignFirstResponder];
    return true;
}

-(void)dismissKeyboard {
    [self.nameTextField resignFirstResponder];
}

#pragma mark - uipicker delegates

// The number of columns of data
- (NSInteger)numberOfComponentsInPickerView:(UIPickerView *)pickerView
{
    return 1;
}

// The number of rows of data
- (NSInteger)pickerView:(UIPickerView *)pickerView numberOfRowsInComponent:(NSInteger)component
{
    return pickerRange.count;
}

// The data to return for the row and component (column) that's being passed in
- (NSString*)pickerView:(UIPickerView *)pickerView titleForRow:(NSInteger)row forComponent:(NSInteger)component
{
    return pickerRange[row];
}

-(void)pickerView:(UIPickerView *)pickerView didSelectRow:(NSInteger)row inComponent:(NSInteger)component {
    currentRange = pickerValues[row];
}

@end
