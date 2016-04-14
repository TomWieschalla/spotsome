//
//  MemoUploadViewController.m
//  SpotSome
//
//  Created by Tom Wieschalla  on 28.07.15.
//  Copyright (c) 2015 SpotSome@Beuth. All rights reserved.
//

#import "MemoUploadViewController.h"
#import "AppConstants.h"
#import "AppDelegate.h"


@implementation MemoUploadViewController
{
    AVAudioRecorder *recorder;
    AVAudioPlayer *player;
    
    BOOL recorded;
    NSString *memoDescription;
    
    AppDelegate *delegate;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    
    delegate = [[UIApplication sharedApplication] delegate];
    
    self.title = @"Audio-Aufnahme";
    
    UITapGestureRecognizer *tap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(dismissKeyboard)];
    [self.view addGestureRecognizer:tap];
    self.descriptiontextField.delegate = self;
    
    recorded = NO;
    
    [self.startButton.layer setBorderWidth:2];
    [self.startButton.layer setBorderColor:[[UIColor clearColor] CGColor]];
    [self.startButton setBackgroundColor:[UIColor blueColor]];
    [self.startButton.layer setCornerRadius:40];
    
    [self.statusButton.layer setBorderWidth:2];
    [self.statusButton.layer setBorderColor:[[UIColor clearColor] CGColor]];
    [self.statusButton.layer setCornerRadius:20];
    
    [self.uploadButton.layer setBorderWidth:2];
    [self.uploadButton.layer setBorderColor:[[UIColor clearColor] CGColor]];
    [self.uploadButton.layer setCornerRadius:10];
    
    if (!recorded) {
        [self.statusButton setAlpha:0.25];
    }
    
    // Set the audio file
    NSArray *pathComponents = [NSArray arrayWithObjects:
                               [NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES) lastObject],
                               @"MyAudioMemo.m4a",
                               nil];
    NSURL *outputFileURL = [NSURL fileURLWithPathComponents:pathComponents];
    
    // Setup audio session
    AVAudioSession *session = [AVAudioSession sharedInstance];
    [session setCategory:AVAudioSessionCategoryPlayAndRecord error:nil];
    
    // Define the recorder setting
    NSMutableDictionary *recordSetting = [[NSMutableDictionary alloc] init];
    
    [recordSetting setValue:[NSNumber numberWithInt:kAudioFormatMPEG4AAC] forKey:AVFormatIDKey];
    [recordSetting setValue:[NSNumber numberWithFloat:44100.0] forKey:AVSampleRateKey];
    [recordSetting setValue:[NSNumber numberWithInt: 2] forKey:AVNumberOfChannelsKey];
    
    // Initiate and prepare the recorder
    recorder = [[AVAudioRecorder alloc] initWithURL:outputFileURL settings:recordSetting error:NULL];
    recorder.delegate = self;
    recorder.meteringEnabled = YES;
    [recorder prepareToRecord];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

- (IBAction)startStopRecording:(id)sender {
    
    if (!recorder.recording) {
        //record
        AVAudioSession *session = [AVAudioSession sharedInstance];
        [session setActive:YES error:nil];
        
        // Start recording
        [recorder record];
        
        [self.startButton setBackgroundColor:[UIColor redColor]];
        [self.startButton.layer setCornerRadius:20];
        [self.startButton setTitle:@"Stop" forState:UIControlStateNormal];
        
    } else {
        //stop
        [recorder stop];
        
        AVAudioSession *audioSession = [AVAudioSession sharedInstance];
        [audioSession setActive:NO error:nil];
        
        [self.startButton setBackgroundColor:[UIColor blueColor]];
        [self.startButton.layer setCornerRadius:40];
        [self.startButton setTitle:@"Start" forState:UIControlStateNormal];
        recorded = YES;
        [self.statusButton setAlpha:1];
    }
}
- (IBAction)uploadMemo:(id)sender {

    if (recorded) {
        if (memoDescription && ![memoDescription isEqualToString:@""]) {
            NSData *memoData = [NSData dataWithContentsOfURL:recorder.url];
            
            //upload to backend
            long statuscode = [self sendSynchronousRequestWithName:self.board.name fileName:@"memo.mp3" message:memoDescription andData:memoData];
            
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
            UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Achtung" message:@"Bitte eine Beschreibung für das Memo wählen" delegate:self cancelButtonTitle:@"Ok" otherButtonTitles: nil];
            [alert show];
        }

    }
}

- (void) audioPlayerDidFinishPlaying:(AVAudioPlayer *)player successfully:(BOOL)flag{
    UIAlertView *alert = [[UIAlertView alloc] initWithTitle: @"Done"
                                                    message: @"Finish playing the recording!"
                                                   delegate: nil
                                          cancelButtonTitle:@"OK"
                                          otherButtonTitles:nil];
    [alert show];
}

- (IBAction)playMemo:(id)sender {
    if (recorded & !recorder.recording) {
        player = [[AVAudioPlayer alloc] initWithContentsOfURL:recorder.url error:nil];
        [player setDelegate:self];
        [player play];
    }
}

#pragma mark - uitextfield delegate

-(BOOL)textFieldShouldReturn:(UITextField *)textField {
    [textField resignFirstResponder];
    memoDescription = textField.text;
    return true;
}

-(void)dismissKeyboard {
    [self.descriptiontextField resignFirstResponder];
}

#pragma mark - http upload method

-(long)sendSynchronousRequestWithName:(NSString*)name fileName:(NSString*)fileName message:(NSString*)relevantMessage andData:(NSData*)relevantData{
    
    NSString *urlString = [NSString stringWithFormat:@"%@/media/upload",BASEURL];
    
    NSMutableURLRequest *request = [NSMutableURLRequest requestWithURL:[NSURL URLWithString:urlString]];
    [request setHTTPMethod:@"POST"];
    
    [request addValue:delegate.token forHTTPHeaderField:@"X-Access-Token"];
    
    NSString *boundary = @"BOUNDARY";
    NSString *contentType = [NSString stringWithFormat:@"multipart/form-data; boundary=%@",boundary];
    [request addValue:contentType forHTTPHeaderField:@"Content-Type"];
    
    NSMutableData *body = [NSMutableData data];
    
    [body appendData:[[NSString stringWithFormat:@"--%@\r\n",boundary] dataUsingEncoding:NSUTF8StringEncoding]];
    [body appendData:[[NSString stringWithFormat:@"Content-Disposition: form-data; name=%@; filename=%@\r\n",@"media",fileName] dataUsingEncoding:NSUTF8StringEncoding]];
    [body appendData:[@"Content-Type: image/jpg\r\n\r\n" dataUsingEncoding:NSUTF8StringEncoding]];
    
    [body appendData:[NSData dataWithData:relevantData]];
    
    [body appendData:[[NSString stringWithFormat:@"\r\n"] dataUsingEncoding:NSUTF8StringEncoding]];
    
    [body appendData:[[NSString stringWithFormat:@"--%@--\r\n",boundary] dataUsingEncoding:NSUTF8StringEncoding]];
    
    [request setHTTPBody:body];
    
    NSHTTPURLResponse *response;
    NSError *error;
    
    NSData *responseData = [NSURLConnection sendSynchronousRequest:request returningResponse:&response error:&error];
    
    if (error) {
        NSLog(@"Error uploading media: %@",error.localizedDescription);
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Fehler" message:@"Fehler auf den SpotSome-Servern, bitte später erneut versuchen" delegate:self cancelButtonTitle:@"Ok" otherButtonTitles:nil];
        [alert show];
        return 500;
    } else {
        NSDictionary *result = [NSJSONSerialization JSONObjectWithData:responseData options:NSJSONReadingAllowFragments error:&error];
        
        NSLog(@"Upload statuscode: %ld",(long)response.statusCode);
        
        return [self sendDescription:relevantMessage afterUploadWithID:[result objectForKey:@"media_id"]];
    }
}

-(long)sendDescription:(NSString*)description afterUploadWithID:(NSNumber*)ID {
    
    NSString *urlString = [NSString stringWithFormat:@"%@/bulletin_board/%@/message",BASEURL,self.board.boardID];
    
    NSError *error = nil;
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] initWithURL:[NSURL URLWithString:urlString]];
    NSHTTPURLResponse *response;
    [request setHTTPMethod:@"POST"];
    [request setValue:delegate.token forHTTPHeaderField:@"X-Access-Token"];
    [request setValue:@"application/json" forHTTPHeaderField:@"Content-Type"];
    NSNumber* time = [NSNumber numberWithLong:[[NSDate date] timeIntervalSince1970]*1000];
    NSDictionary *bodyDic = @{@"message_text":description,@"media_id":[ID stringValue],@"created_on":[time stringValue]};
    [request setHTTPBody:[NSJSONSerialization dataWithJSONObject:bodyDic options:NSJSONWritingPrettyPrinted error:&error]];
    
    NSData *responseData = [NSURLConnection sendSynchronousRequest:request returningResponse:&response error:&error];
    
    NSLog(@"Description statuscode: %ld",(long)response.statusCode);
    
    return response.statusCode;
}

@end
