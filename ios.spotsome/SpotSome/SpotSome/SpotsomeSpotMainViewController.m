//
//  SpotsomeSpotMainViewController.m
//  SpotSome
//
//  Created by Tom Wieschalla  on 08.07.15.
//  Copyright (c) 2015 SpotSome@Beuth. All rights reserved.
//

#import "SpotsomeSpotMainViewController.h"
#import "YelpAddressTableViewCell.h"
#import "PushTableViewCell.h"
#import "BoardTableViewController.h"
#import "ChatTableViewController.h"
#import "SBoard.h"
#import "SChat.h"
#import "SLocation.h"
#import "AppConstants.h"
#import "AppDelegate.h"
#import "JSQMessage.h"

@implementation SpotsomeSpotMainViewController {
    AppDelegate *delegate;
    SBoard *bulletinboard;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    
    delegate = [[UIApplication sharedApplication] delegate];
    
    self.tableView.tableFooterView = [[UIView alloc] initWithFrame:CGRectZero];
    
    [self.tableView registerNib:[UINib nibWithNibName:@"YelpAddressTableViewCell" bundle:nil] forCellReuseIdentifier:@"first"];
    [self.tableView registerNib:[UINib nibWithNibName:@"PushTableViewCell" bundle:nil] forCellReuseIdentifier:@"second"];
    [self.tableView registerNib:[UINib nibWithNibName:@"PushTableViewCell" bundle:nil] forCellReuseIdentifier:@"third"];
    bulletinboard = [self getBoard];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark - Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    
    return 3;
}

-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    CGFloat height = 0;
    
    if (indexPath.row == 0) {
        
        height = 135;
        
    } else {
        
        height = 50;
        
    }
    
    return height;
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    UITableViewCell *cell = nil;
    
    if (indexPath.row == 0) {
        YelpAddressTableViewCell *yelpCell = (YelpAddressTableViewCell *)[tableView dequeueReusableCellWithIdentifier:@"first"];
        yelpCell.streetLabel.text = self.street;
        yelpCell.zipcodeLabel.text = self.zipcode;
        yelpCell.countryLabel.text = self.country;
        cell = yelpCell;
    } else if (indexPath.row == 1){
        PushTableViewCell *pushCell = [tableView dequeueReusableCellWithIdentifier:@"third"];
        pushCell.textLabel.text = @"Zum Spotboard";
        cell = pushCell;
    } else if (indexPath.row == 2){
        PushTableViewCell *pushCell = [tableView dequeueReusableCellWithIdentifier:@"third"];
        pushCell.textLabel.text = @"Zum Spotchat";
        [pushCell.imageView setImage:[UIImage imageNamed:@"Chat_Filled_100_blue"]];
        cell = pushCell;
    }
    
    return cell;
}

-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    
    UIStoryboard *sb = [UIStoryboard storyboardWithName:@"Main" bundle:nil];
    
    if (indexPath.row == 1) {
        
        if (bulletinboard) {
            BoardTableViewController *board = [sb instantiateViewControllerWithIdentifier:@"board"];
            board.spot = self.spot;
            board.board = bulletinboard;
            [self.navigationController pushViewController:board animated:YES];
        } else {
            UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Fehler" message:@"Kein Board an diesem Spot vorhanden!" delegate:self cancelButtonTitle:@"Ok" otherButtonTitles: nil];
            [alert show];
        }

        
    } else if (indexPath.row == 2){
        
        ChatTableViewController *chatC = [sb instantiateViewControllerWithIdentifier:@"chat"];
        chatC.spot = self.spot;
        [self.navigationController pushViewController:chatC animated:YES];
    }
}

#pragma mark - spotsome methods

-(SBoard*)getBoard {
    
    SBoard *board = nil;
    
    //sending post request for auth in java backend
    NSString *urlString = [NSString stringWithFormat:@"%@/bulletin_board?spot_id=%@",BASEURL,self.spot.id];
    
    NSError *error = nil;
    NSURLResponse *response = nil;
    
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] initWithURL:[NSURL URLWithString:urlString]];
    [request setHTTPMethod:@"GET"];
    [request setValue:delegate.token forHTTPHeaderField:@"X-Access-Token"];
    
    NSData *responseData = [NSURLConnection sendSynchronousRequest:request returningResponse:&response error:&error];
    
    //testing
    NSHTTPURLResponse* httpResponse = (NSHTTPURLResponse*)response;
    long statuscode = [httpResponse statusCode];
    NSLog(@"Get Board Statuscode: %ld",statuscode);
    
    if (error) {
        NSLog(@"Error getting board: %@",error);
    } else {
        if (statuscode == 200) {
            NSArray *result = [NSJSONSerialization JSONObjectWithData:responseData options:kNilOptions error:&error];
            if (error) {
                NSLog(@"Error getting Spot NSJSONSerialization: %@",error.localizedDescription);
            } else {
                
                NSDictionary *boardDic = result[0];
                
                NSDictionary *spotDic = [boardDic objectForKey:@"spot"];
                NSDictionary *locationDic = [spotDic objectForKey:@"location"];
                
                SLocation *location = [[SLocation alloc] initWithId:[locationDic objectForKey:@"location_id"] latitude:[locationDic objectForKey:@"latitude"] andLongitude:[locationDic objectForKey:@"longitude"]];
                SSpot *spot = [[SSpot alloc] initWithId:[spotDic objectForKey:@"spot_id"] name:[spotDic objectForKey:@"name"] radius:[spotDic objectForKey:@"radius"] andLocation:location];
                board = [[SBoard alloc] initWithName:[boardDic objectForKey:@"name"] spot:spot messages:[boardDic objectForKey:@"messages"] andBoardID:[boardDic objectForKey:@"bulletin_board_id"]];
            }
        } else {
            NSLog(@"Statuscode: %ld error: %@",statuscode,[[NSString alloc] initWithData:responseData encoding:NSUTF8StringEncoding]);
        }
    }
    
    return board;
}

@end
