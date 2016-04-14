//
//  LoginViewController.m
//  SpotSome
//
//  Created by Tom Wieschalla  on 22.06.15.
//  Copyright (c) 2015 SpotSome@Beuth. All rights reserved.
//

#import "LoginViewController.h"
#import "MainViewController.h"
#import <Fabric/Fabric.h>
#import <TwitterKit/TwitterKit.h>
#import "AppDelegate.h"
#import "AppConstants.h"

@implementation LoginViewController {
    AppDelegate *delegate;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    
    delegate = [[UIApplication sharedApplication] delegate];
    
    TWTRLogInButton *logInButton = [TWTRLogInButton buttonWithLogInCompletion:^(TWTRSession *session, NSError *error) {
        if (session && [self requestOAuthEcho]) {
            
            NSLog(@"signed in as %@", [session userName]);
            
        } else {
            NSLog(@"error: %@", [error localizedDescription]);
        }

    }];
    logInButton.center = CGPointMake(self.view.center.x, self.view.center.y + self.twitterImage.bounds.size.height);
    [self.view addSubview:logInButton];

    if (false) {
        delegate.token = @"u3vnfjmn6a4murof06q0j1loq4";
        delegate.userID = [NSNumber numberWithInt:1];
        delegate.twitterName = @"spotsomeios";
        MainViewController *mv = [[MainViewController alloc] initWithNibName:@"MainViewController" bundle:nil];
        
        UINavigationController *nav = [[UINavigationController alloc] initWithRootViewController:mv];
        [self.navigationController presentViewController:nav animated:YES completion:nil];
    }
    
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (BOOL)requestOAuthEcho {
    
    TWTROAuthSigning *oauthSigning = [[TWTROAuthSigning alloc] initWithAuthConfig:[Twitter sharedInstance].authConfig authSession:[[Twitter sharedInstance] sessionStore].session];
    NSDictionary *authHeaders = [oauthSigning OAuthEchoHeadersToVerifyCredentials];
    
    if ([self sendOAuthRegisterRequestToBackendWithCredentials:authHeaders]) {
        return true;
    } else {
        return false;
    }
}

- (BOOL)sendOAuthRegisterRequestToBackendWithCredentials:(NSDictionary *)credentials{
    
    //sending post request for auth in java backend
    NSString *urlString = [NSString stringWithFormat:@"%@/account/register",BASEURL];
    
    NSError *error = nil;
    NSURLResponse *response;
    
    //basic authentification
    NSString *authStr = [NSString stringWithFormat:@"remote-x4D9Tu:cX1TdaFKR9"];
    NSData *authData = [authStr dataUsingEncoding:NSASCIIStringEncoding];
    NSString *authValue = [NSString stringWithFormat:@"Basic %@", [authData base64Encoding]];
    
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] initWithURL:[NSURL URLWithString:urlString]];
    
    NSDictionary *request_body = @{ @"device_token" : @"foo"};
    
    [request setHTTPMethod:@"POST"];
    [request setValue:[credentials objectForKey:@"X-Verify-Credentials-Authorization"] forHTTPHeaderField:@"X-Verify-Credentials-Authorization"];
    [request setValue:delegate.token forHTTPHeaderField:@"X-Access-Token"];
    [request setValue:authValue forHTTPHeaderField:@"Authorization"];
    [request setHTTPBody:[NSJSONSerialization dataWithJSONObject:request_body options:NSJSONWritingPrettyPrinted error:&error]];
    
    
    NSData *responseData = [NSURLConnection sendSynchronousRequest:request returningResponse:&response error:&error];
    
    //testing
    NSHTTPURLResponse* httpResponse = (NSHTTPURLResponse*)response;
    long statuscode = [httpResponse statusCode];
    NSLog(@"Register Statuscode: %ld",statuscode);
    
    if (error) {
        NSLog(@"Backend Authentification failed: %@",error.localizedDescription);
        return false;
    } else {
        NSDictionary *result = [NSJSONSerialization JSONObjectWithData:responseData options:kNilOptions error:&error];
        NSDictionary *tokenDic = [result objectForKey:@"accessToken"];
        
        // first test Token: 12ij9o5cs5q0pkl3hefeeldeie
        if (statuscode == 201 && !error) {
            NSLog(@"Current token: %@",[tokenDic objectForKey:@"token"]);
            delegate.token = [tokenDic objectForKey:@"token"];
            delegate.twitterName = [result objectForKey:@"username"];
            delegate.userID = [result objectForKey:@"userId"];
            MainViewController *mv = [[MainViewController alloc] initWithNibName:@"MainViewController" bundle:nil];
            
            UINavigationController *nav = [[UINavigationController alloc] initWithRootViewController:mv];
            [self.navigationController presentViewController:nav animated:YES completion:nil];
            
            return true;
        } else {
            if (statuscode == 409) {
                return [self sendOAuthRequestToBackendWithoutCredentials];
            } else {
                NSLog(@"Error from Backend: %@",[result objectForKey:@"message"]);
                return false;
            }
        }
    }
}

- (BOOL)sendOAuthRequestToBackendWithoutCredentials {
    
    NSError *error = nil;
    NSURLResponse *response = nil;
    
    TWTROAuthSigning *oauthSigning = [[TWTROAuthSigning alloc] initWithAuthConfig:[Twitter sharedInstance].authConfig authSession:[Twitter sharedInstance].version];
    NSDictionary *authHeaders = [oauthSigning OAuthEchoHeadersToVerifyCredentials];

    //sending post request for auth in java backend
    NSString *urlString = [NSString stringWithFormat:@"%@/account/access_token",BASEURL];
    
    //basic authentification
    NSString *authStr = [NSString stringWithFormat:@"remote-x4D9Tu:cX1TdaFKR9"];
    NSData *authData = [authStr dataUsingEncoding:NSASCIIStringEncoding];
    NSString *authValue = [NSString stringWithFormat:@"Basic %@", [authData base64Encoding]];
    
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] initWithURL:[NSURL URLWithString:urlString]];
    [request setHTTPMethod:@"GET"];
    [request setValue:[authHeaders objectForKey:@"X-Verify-Credentials-Authorization"] forHTTPHeaderField:@"X-Verify-Credentials-Authorization"];
    [request setValue:authValue forHTTPHeaderField:@"Authorization"];

    NSData *responseData = [NSURLConnection sendSynchronousRequest:request returningResponse:&response error:&error];
    
    //testing
    NSHTTPURLResponse* httpResponse = (NSHTTPURLResponse*)response;
    long statuscode = [httpResponse statusCode];
    NSLog(@"Login Statuscode: %ld",statuscode);
    
    if (error) {
        NSLog(@"Backend Authentification failed: %@",error.localizedDescription);
        return false;
    } else {
        NSDictionary *result = [NSJSONSerialization JSONObjectWithData:responseData options:kNilOptions error:&error];
        NSDictionary *tokenDic = [result objectForKey:@"accessToken"];
        
        // first test Token: hhfoctse0mcnmcas9cnt4kl5b9
        if (statuscode == 200 && !error) {
            NSLog(@"Current token: %@",[tokenDic objectForKey:@"token"]);
            delegate.token = [tokenDic objectForKey:@"token"];
            delegate.twitterName = [result objectForKey:@"username"];
            delegate.userID = [result objectForKey:@"userId"];
            MainViewController *mv = [[MainViewController alloc] initWithNibName:@"MainViewController" bundle:nil];
            
            UINavigationController *nav = [[UINavigationController alloc] initWithRootViewController:mv];
            [self.navigationController presentViewController:nav animated:YES completion:nil];
            
            return true;
        } else {
            NSLog(@"Error from Backend: %@",[result objectForKey:@"message"]);
            return false;
        }
    }
}

@end
