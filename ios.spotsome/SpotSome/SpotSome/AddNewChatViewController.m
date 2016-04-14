//
//  AddNewChatViewController.m
//  SpotSome
//
//  Created by Tom Wieschalla  on 29.07.15.
//  Copyright (c) 2015 SpotSome@Beuth. All rights reserved.
//

#import "AddNewChatViewController.h"
#import "AppConstants.h"
#import "AppDelegate.h"

@implementation AddNewChatViewController {
    bool firstAdd;
    AppDelegate *delegate;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    
    delegate = [[UIApplication sharedApplication] delegate];
    
    self.title = @"Chat-Erstellung";
    
    self.titelTextView.delegate = self;
    firstAdd = YES;
}

-(BOOL)automaticallyAdjustsScrollViewInsets {
    return NO;
}

-(void)textViewDidBeginEditing:(UITextView *)textView {
    
    if (firstAdd) {
        self.titelTextView.text = @"";
        [self.titelTextView setTextColor:[UIColor blackColor]];
        firstAdd = NO;
    }
}

-(BOOL)textView:(UITextView *)textView shouldChangeTextInRange:(NSRange)range replacementText:(NSString *)text {
    NSCharacterSet *doneButtonCharacterSet = [NSCharacterSet newlineCharacterSet];
    NSRange replacementTextRange = [text rangeOfCharacterFromSet:doneButtonCharacterSet];
    NSUInteger location = replacementTextRange.location;
    
    if (textView.text.length + (text.length - range.length) >= 58){
        if (location != NSNotFound){
            [textView resignFirstResponder];
        }
        return NO;
    }
    else if (location != NSNotFound){
        [textView resignFirstResponder];
        return NO;
    }
    return YES;
}

- (IBAction)addChat:(id)sender {
    
    if (!firstAdd && self.titelTextView.text != @"") {
        //sending post request for auth in java backend
        NSString *urlString = [NSString stringWithFormat:@"%@/chat",BASEURL];
        
        NSError *error = nil;
        NSHTTPURLResponse *response;
        
        NSMutableURLRequest *request = [[NSMutableURLRequest alloc] initWithURL:[NSURL URLWithString:urlString]];
        
        NSDictionary *request_body = @{
                                       @"spot_id" : self.spot.id,
                                       @"name": self.titelTextView.text};
        
        [request setHTTPMethod:@"POST"];
        [request setValue:delegate.token forHTTPHeaderField:@"X-Access-Token"];
        [request setValue:@"application/json" forHTTPHeaderField:@"Content-Type"];
        [request setHTTPBody:[NSJSONSerialization dataWithJSONObject:request_body options:NSJSONWritingPrettyPrinted error:&error]];
        
        
        NSData *responseData = [NSURLConnection sendSynchronousRequest:request returningResponse:&response error:&error];
        
        if (error) {
            NSLog(@"Error: %@",error.localizedDescription);
            NSLog(@"Error with statuscode: %ld",(long)response.statusCode);
        } else if (response.statusCode == 201){
            //navigate back
            NSArray *controller = self.navigationController.viewControllers;
            [self.navigationController popToViewController:controller[controller.count-2] animated:YES];
            UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Erfolg" message:@"Chat wurde erfolgreich erstellt" delegate:self cancelButtonTitle:@"Ok" otherButtonTitles:nil];
            [alert show];
        } else {
            NSLog(@"Error with statuscode: %ld",(long)response.statusCode);
            UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Fehler" message:@"Ein Fehler ist aufgetreten, bitte später erneut versuchen" delegate:self cancelButtonTitle:@"Ok" otherButtonTitles:nil];
            [alert show];
        }
    } else {
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Fehler" message:@"Bitte einen Titel für den Chat eingeben" delegate:self cancelButtonTitle:@"Ok" otherButtonTitles:nil];
        [alert show];
    }

}
@end
