//
//  MessageUploadViewController.m
//  SpotSome
//
//  Created by Tom Wieschalla  on 28.07.15.
//  Copyright (c) 2015 SpotSome@Beuth. All rights reserved.
//

#import "MessageUploadViewController.h"
#import "AppConstants.h"
#import "AppDelegate.h"

@implementation MessageUploadViewController
{
    NSString *message;
    bool firstAction;
    AppDelegate *delegate;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    
    delegate = [[UIApplication sharedApplication] delegate];
    
    firstAction = YES;
    
    self.title = @"Nachrichten";
    
    UITapGestureRecognizer *tap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(dismissKeyboard)];
    [self.view addGestureRecognizer:tap];
    self.textView.delegate = self;
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)uploadMessage:(id)sender {
    
    if (message) {
        //upload to backend
        long statuscode = [self sendDescription:message afterUploadWithID:nil];
        
        if (statuscode == 201) {
            //navigate back
            NSArray *controller = self.navigationController.viewControllers;
            [self.navigationController popToViewController:controller[controller.count-3] animated:YES];
            UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Erfolg" message:@"Medium wurde erfolgreich hochgeladen" delegate:self cancelButtonTitle:@"Ok" otherButtonTitles:nil];
            [alert show];
        } else {
            NSLog(@"Error uploading Message statuscode: %ld",statuscode);
            UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Fehler" message:@"Fehler auf den SpotSome-Servern, bitte später erneut versuchen" delegate:self cancelButtonTitle:@"Ok" otherButtonTitles:nil];
            [alert show];
        }
    } else {
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Achtung" message:@"Bitte geben Sie eine Nachricht ein" delegate:self cancelButtonTitle:@"Ok" otherButtonTitles: nil];
        [alert show];
    }
}

#pragma mark - uitextview delegate

-(BOOL)textView:(UITextView *)textView shouldChangeTextInRange:(NSRange)range replacementText:(NSString *)text {
    NSCharacterSet *doneButtonCharacterSet = [NSCharacterSet newlineCharacterSet];
    NSRange replacementTextRange = [text rangeOfCharacterFromSet:doneButtonCharacterSet];
    NSUInteger location = replacementTextRange.location;
    
    if (textView.text.length + text.length > 140){
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

-(void)textViewDidEndEditing:(UITextView *)textView {
    message = textView.text;
}

-(void)textViewDidBeginEditing:(UITextView *)textView {
    if (firstAction) {
        textView.text = @"";
        firstAction = NO;
    }
}

#pragma mark - http upload method

-(long)sendDescription:(NSString*)description afterUploadWithID:(NSNumber*)ID {
    
    NSString *urlString = [NSString stringWithFormat:@"%@/bulletin_board/%@/message",BASEURL,self.board.boardID];
    
    NSError *error = nil;
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] initWithURL:[NSURL URLWithString:urlString]];
    NSHTTPURLResponse *response;
    [request setHTTPMethod:@"POST"];
    [request setValue:delegate.token forHTTPHeaderField:@"X-Access-Token"];
    [request setValue:@"application/json" forHTTPHeaderField:@"Content-Type"];
    NSNumber* time = [NSNumber numberWithLong:[[NSDate date] timeIntervalSince1970]*1000];
    NSDictionary *bodyDic = @{@"message_text":description,@"media_id":[NSNull null],@"created_on":[time stringValue]};
    [request setHTTPBody:[NSJSONSerialization dataWithJSONObject:bodyDic options:NSJSONWritingPrettyPrinted error:&error]];
    
    NSData *responseData = [NSURLConnection sendSynchronousRequest:request returningResponse:&response error:&error];
    
    NSLog(@"Description statuscode: %ld",(long)response.statusCode);
    
    return response.statusCode;
}

@end
