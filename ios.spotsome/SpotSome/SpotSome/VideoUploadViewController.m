//
//  VideoUploadViewController.m
//  SpotSome
//
//  Created by Tom Wieschalla  on 28.07.15.
//  Copyright (c) 2015 SpotSome@Beuth. All rights reserved.
//

#import "VideoUploadViewController.h"
#import <AVFoundation/AVFoundation.h>
#import "AppConstants.h"
#import "AppDelegate.h"

@implementation VideoUploadViewController
{
    NSString *moviePath;
    NSString *movieDescription;
    AppDelegate *delegate;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    
    delegate = [[UIApplication sharedApplication] delegate];
    
    self.title = @"Video/Foto-Upload";
    
    UITapGestureRecognizer *movieTap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(showMovie)];
    [self.imageView addGestureRecognizer:movieTap];
    self.imageView.userInteractionEnabled = YES;
    
    UITapGestureRecognizer *tap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(dismissKeyboard)];
    [self.view addGestureRecognizer:tap];
    self.descriptionTextField.delegate = self;
    
    //customize buttons
    [self.takeButton.layer setBorderWidth:2];
    [self.takeButton.layer setBorderColor:[[UIColor clearColor] CGColor]];
    [self.takeButton.layer setCornerRadius:10];
    
    [self.makeButton.layer setBorderWidth:2];
    [self.makeButton.layer setBorderColor:[[UIColor clearColor] CGColor]];
    [self.makeButton.layer setCornerRadius:10];
    
    [self.uploadButton.layer setBorderWidth:2];
    [self.uploadButton.layer setBorderColor:[[UIColor clearColor] CGColor]];
    [self.uploadButton.layer setCornerRadius:10];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark - video-board methods

- (IBAction)createVideo:(id)sender {
    
    UIImagePickerController *picker = [UIImagePickerController new];
    picker.delegate = self;
    picker.allowsEditing = YES;
    picker.sourceType = UIImagePickerControllerSourceTypeCamera;
    picker.mediaTypes = [[NSArray alloc] initWithObjects:(NSString*) kUTTypeMovie,(NSString*) kUTTypeImage, nil];
    picker.videoQuality = UIImagePickerControllerQualityTypeMedium;
    picker.videoMaximumDuration = 180;

    
    [self presentViewController:picker animated:YES completion:nil];
}

- (IBAction)getVideo:(id)sender {
    
    UIImagePickerController *picker = [UIImagePickerController new];
    picker.delegate = self;
    picker.allowsEditing = YES;
    picker.sourceType = UIImagePickerControllerSourceTypePhotoLibrary;
    picker.mediaTypes = [[NSArray alloc] initWithObjects:(NSString*) kUTTypeMovie,(NSString*) kUTTypeImage, nil];
    
    [self presentViewController:picker animated:YES completion:nil];
}


-(void)showMovie {
    
    if (moviePath) {
        MPMoviePlayerViewController *theMovie = [[MPMoviePlayerViewController alloc]
                                                 initWithContentURL:[NSURL fileURLWithPath:moviePath]];
        
        [self presentMoviePlayerViewControllerAnimated:theMovie];
    }
    
}

- (IBAction)uploadToBoard:(id)sender {
    
    if (moviePath) {
        NSData *videoData = [NSData dataWithContentsOfFile:moviePath];
        
        if (movieDescription && ![movieDescription isEqualToString:@""]) {
            
            //upload to backend
            long statuscode = [self sendSynchronousRequestWithName:self.board.name fileName:@"movie.mpeg" message:movieDescription andData:videoData];
            
            if (statuscode == 201) {
                //navigate back
                NSArray *controller = self.navigationController.viewControllers;
                [self.navigationController popToViewController:controller[controller.count-3] animated:YES];
            } else {
                NSLog(@"Error uploading Message statuscode: %ld",statuscode);
                UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Fehler" message:@"Fehler auf den SpotSome-Servern, bitte später erneut versuchen" delegate:self cancelButtonTitle:@"Ok" otherButtonTitles:nil];
                [alert show];
            }
            
        } else {
            UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Achtung" message:@"Bitte eine Beschreibung für das Video wählen" delegate:self cancelButtonTitle:@"Ok" otherButtonTitles: nil];
            [alert show];
        }
        
    } else if (self.imageView.image) {
        NSData *pictureData = UIImagePNGRepresentation(self.imageView.image);
        
        if (movieDescription) {
            
            //upload to backend
            long statuscode = [self sendSynchronousRequestWithName:self.board.name fileName:@"photo.png" message:movieDescription andData:pictureData];
            
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
            UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Achtung" message:@"Bitte eine Beschreibung für das Video wählen" delegate:self cancelButtonTitle:@"Ok" otherButtonTitles: nil];
            [alert show];
        }
        
    } else {
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Achtung" message:@"Wählen Sie zuerst ein Video aus" delegate:self cancelButtonTitle:@"Ok" otherButtonTitles: nil];
        [alert show];
    }
    
    
}

#pragma mark - picker methods

- (void)imagePickerController:(UIImagePickerController *)picker didFinishPickingMediaWithInfo:(NSDictionary *)info {
    
    if (picker.sourceType == 1) {
        if ([[info objectForKey:UIImagePickerControllerMediaType] isEqualToString:@"public.image"]) {
            UIImage *chosenImage = info[UIImagePickerControllerEditedImage];
            self.imageView.image = chosenImage;
            [self dismissViewControllerAnimated:YES completion:nil];
            moviePath = nil;
        } else {
            NSString *mediaType = [info objectForKey: UIImagePickerControllerMediaType];
            [self dismissViewControllerAnimated:YES completion:nil];
            // Handle a movie capture
            if (CFStringCompare ((__bridge_retained CFStringRef) mediaType, kUTTypeMovie, 0) == kCFCompareEqualTo) {
                moviePath = [[info objectForKey:UIImagePickerControllerMediaURL] path];
                
                self.imageView.image = [self generateThumbImage:moviePath];
                
                if (UIVideoAtPathIsCompatibleWithSavedPhotosAlbum(moviePath)) {
                    UISaveVideoAtPathToSavedPhotosAlbum(moviePath, self,
                                                        @selector(video:didFinishSavingWithError:contextInfo:), nil);
                } 
            }
        }
    } else {
        if ([[info objectForKey:UIImagePickerControllerMediaType] isEqualToString:@"public.image"]) {
            UIImage *chosenImage = info[UIImagePickerControllerEditedImage];
            self.imageView.image = chosenImage;
            [self dismissViewControllerAnimated:YES completion:nil];
            moviePath = nil;
            
        } else {
        
            [self dismissViewControllerAnimated:YES completion:nil];
            moviePath = [[info objectForKey:UIImagePickerControllerMediaURL] path];
            
            self.imageView.image = [self generateThumbImage:moviePath];
        }
    }
    
}

- (void)imagePickerControllerDidCancel:(UIImagePickerController *)picker {
    
    [picker dismissViewControllerAnimated:YES completion:NULL];
    
}

-(void)video:(NSString*)videoPath didFinishSavingWithError:(NSError*)error contextInfo:(void*)contextInfo {
    if (error) {
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Error" message:@"Video Saving Failed"
                                                       delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil];
        [alert show];
    } else {
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Video Saved" message:@"Saved To Photo Album"
                                                       delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil];
        [alert show];
    }
}

-(UIImage *)generateThumbImage:(NSString *)filepath {
    NSURL *url = [NSURL fileURLWithPath:filepath];
    
    AVAsset *asset = [AVAsset assetWithURL:url];
    AVAssetImageGenerator *imageGenerator = [[AVAssetImageGenerator alloc]initWithAsset:asset];
    CMTime time = [asset duration];
    time.value = 0;
    CGImageRef imageRef = [imageGenerator copyCGImageAtTime:time actualTime:NULL error:NULL];
    UIImage *thumbnail = [UIImage imageWithCGImage:imageRef];
    CGImageRelease(imageRef);  // CGImageRef won't be released by ARC
    
    return thumbnail;
}

#pragma mark - uitextfield delegate

-(BOOL)textFieldShouldReturn:(UITextField *)textField {
    [textField resignFirstResponder];
    movieDescription = textField.text;
    return true;
}

-(void)dismissKeyboard {
    [self.descriptionTextField resignFirstResponder];
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
