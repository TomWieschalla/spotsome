//
//  ChatTableViewController.m
//  SpotSome
//
//  Created by Tom Wieschalla  on 08.07.15.
//  Copyright (c) 2015 SpotSome@Beuth. All rights reserved.
//

#import "ChatTableViewController.h"
#import "MessagesViewController.h"
#import "ChatItemTableViewCell.h"
#import "AppDelegate.h"
#import "AppConstants.h"
#import "AddNewChatViewController.h"
#import "SLocation.h"

@implementation ChatTableViewController
{
    AppDelegate *delegate;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    
    delegate = [[UIApplication sharedApplication] delegate];
    
    self.title = @"Spotchat";
    
    UIBarButtonItem *btn = [[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemAdd target:self action:@selector(openAddChatView)];
    [self.navigationItem setRightBarButtonItem:btn];
}

-(void)viewWillAppear:(BOOL)animated {
    self.chats = [self getChats];
    [self.tableView reloadData];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark - Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    
    return 1;
}

-(NSString *)tableView:(UITableView *)tableView titleForHeaderInSection:(NSInteger)section {
    return @"Chats";
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    
    if (self.chats.count == 0) {
        return 1;
    }else {
        return self.chats.count;
    }
}

-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    return 60;
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    UITableViewCell *cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:@"default"];
    
    if (self.chats.count == 0) {
        cell.textLabel.text = @"Keine Chats Vorhanden";
        cell.userInteractionEnabled = NO;
        cell.accessoryType = UITableViewCellAccessoryNone;
    } else {
        NSArray *nib = [[NSBundle mainBundle] loadNibNamed:@"ChatItemTableViewCell" owner:self options:nil];
        ChatItemTableViewCell *newcell = [nib objectAtIndex:0];
        SChat *chat = self.chats[indexPath.row];
        newcell.titleLabel.text = chat.name;
        
        cell = newcell;
    }
    
    return cell;
}

-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    
    MessagesViewController *vc = [MessagesViewController new];
    
    SChat *chat = self.chats[indexPath.row];

    vc.senderId = [delegate.userID stringValue];
    vc.senderDisplayName = delegate.twitterName;

    //Sort messages
    NSSortDescriptor *sortDescriptor;
    sortDescriptor = [[NSSortDescriptor alloc] initWithKey:@"messageId"
                                                 ascending:YES];
    NSArray *sortDescriptors = [NSArray arrayWithObject:sortDescriptor];
    
    vc.chatData = [[NSMutableArray alloc] initWithArray:[chat.messages sortedArrayUsingDescriptors:sortDescriptors]];
    vc.title = chat.name;
    vc.chat = chat;
    [self.navigationController pushViewController:vc animated:YES];
}

//-(NSMutableArray *)getAllChats {
//    
//    NSMutableArray *medias = [NSMutableArray new];
//    
//    NSString *urlString = [NSString stringWithFormat:@"%@/chat/%@/message",BASEURL,self.chat.chatID];
//    
//    NSError *error = nil;
//    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] initWithURL:[NSURL URLWithString:urlString]];
//    NSHTTPURLResponse *response;
//    [request setHTTPMethod:@"GET"];
//    [request setValue:delegate.token forHTTPHeaderField:@"X-Access-Token"];
//    
//    NSData *responseData = [NSURLConnection sendSynchronousRequest:request returningResponse:&response error:&error];
//    
//    NSArray *result = [NSJSONSerialization JSONObjectWithData:responseData options:NSJSONReadingAllowFragments error:&error];
//    
//    NSString *errorString = [[NSString alloc] initWithData:responseData encoding:NSUTF8StringEncoding];
//    NSLog(@"error: %@, statuscode: %ld",errorString,(long)response.statusCode);
//    
//    for (NSDictionary *dic1 in result) {
//
//    }
//    
//    return medias;
//}

-(void)openAddChatView {
    
    AddNewChatViewController *vc = [AddNewChatViewController new];
    vc.spot = self.spot;
    
    [self.navigationController pushViewController:vc animated:YES];
}

-(NSMutableArray*)getChats {
    
    NSMutableArray *allChats = [NSMutableArray new];
    SChat *chat = nil;
    
    //sending post request for auth in java backend
    NSString *urlString = [NSString stringWithFormat:@"%@/chat?spot_id=%@",BASEURL,self.spot.id];
    
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
                
                for (NSDictionary *chatDic in result) {
                    NSDictionary *spotDic = [chatDic objectForKey:@"spot"];
                    NSDictionary *locationDic = [spotDic objectForKey:@"location"];
                    
                    SLocation *location = [[SLocation alloc] initWithId:[locationDic objectForKey:@"location_id"] latitude:[locationDic objectForKey:@"latitude"] andLongitude:[locationDic objectForKey:@"longitude"]];
                    SSpot *spot = [[SSpot alloc] initWithId:[spotDic objectForKey:@"spot_id"] name:[spotDic objectForKey:@"name"] radius:[spotDic objectForKey:@"radius"] andLocation:location];
                    chat = [[SChat alloc] initWithName:[chatDic objectForKey:@"name"] spot:spot messages:[chatDic objectForKey:@"messages"] andChatID:[chatDic objectForKey:@"chat_id"]];
                    [allChats addObject:[self serializeMessagesFromChat:chat]];
                }
                
            }
        } else {
            NSLog(@"Statuscode: %ld error: %@",statuscode,[[NSString alloc] initWithData:responseData encoding:NSUTF8StringEncoding]);
        }
    }
    
    return allChats;
}

-(SChat *)serializeMessagesFromChat:(SChat*)chat {
    
    NSMutableArray *resultArray = [NSMutableArray new];
    
    for (NSDictionary *messageDic in chat.messages) {
        NSDictionary *authorDic = [messageDic objectForKey:@"author"];
        NSString *messageId = [[messageDic objectForKey:@"message_id"] stringValue];
        NSString *userId = [[authorDic objectForKey:@"userId"] stringValue];
        NSString *userName = [authorDic objectForKey:@"username"];
        NSDate *date = [[NSDate alloc] initWithTimeIntervalSince1970:[[messageDic objectForKey:@"created_on"] doubleValue]*1000];
        NSString *text = [messageDic objectForKey:@"message_text"];
        
        JSQMessage *message = [[JSQMessage alloc] initWithSenderId:userId messageId:messageId senderDisplayName:userName date:date text:text];
        [resultArray addObject:message];
    }
    
    chat.messages = resultArray;
    
    return chat;
}

@end
