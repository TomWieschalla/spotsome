//
//  BoardTableViewController.m
//  SpotSome
//
//  Created by Tom Wieschalla  on 08.07.15.
//  Copyright (c) 2015 SpotSome@Beuth. All rights reserved.
//

#import "BoardTableViewController.h"
#import "BoardItemTableViewCell.h"
#import "VideoPresentationViewController.h"
#import "MemoPresentationViewController.h"
#import "MessagePresentationViewController.h"
#import "UploadViewController.h"
#import "AppConstants.h"
#import "AppDelegate.h"
#import "SMedia.h"

@implementation BoardTableViewController {
    AppDelegate *delegate;
    NSMutableArray *mediaArray;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    
    delegate = [[UIApplication sharedApplication] delegate];
    mediaArray = [NSMutableArray new];
    
    mediaArray = [self getAllMessages];
    self.title = @"Spotboard";
    
    UIBarButtonItem *btn = [[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemAdd target:self action:@selector(openUploadView)];
    [self.navigationItem setRightBarButtonItem:btn];

}

-(void)viewDidAppear:(BOOL)animated {
    
    mediaArray = [self getAllMessages];
    [self.tableView reloadData];
}

#pragma mark - Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {

    return 1;
}

-(NSString *)tableView:(UITableView *)tableView titleForHeaderInSection:(NSInteger)section {
    return @"Medien";
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {

    if (mediaArray.count == 0) {
        return 1;
    } else {
        return mediaArray.count;
    }
}

-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    return 60;
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    UITableViewCell *cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:@"default"];
    
    if (mediaArray.count == 0) {
        cell.textLabel.text = @"Keine Medien Vorhanden";
        cell.userInteractionEnabled = NO;
        cell.accessoryType = UITableViewCellAccessoryNone;
    } else {
        NSArray *nib = [[NSBundle mainBundle] loadNibNamed:@"BoardItemTableViewCell" owner:self options:nil];
        BoardItemTableViewCell *newcell = [nib objectAtIndex:0];
        SMedia *media = mediaArray[indexPath.row];
        newcell.titleLabel.text = media.messageText;
        newcell.subtitleLabel.text = [NSDateFormatter
                                      localizedStringFromDate:media.date dateStyle:NSDateFormatterShortStyle timeStyle:NSDateFormatterShortStyle];
        if ([media.mime_type isEqualToString:@"audio/mp4"]) {
            [newcell.iconImageView setImage:[UIImage imageNamed:@"Micro_50"]];
        } else if ([media.mime_type isEqualToString:@"text"]) {
            [newcell.iconImageView setImage:[UIImage imageNamed:@"New_50"]];
        } else {
            [newcell.iconImageView setImage:[UIImage imageNamed:@"Film_50"]];
        }
        
        cell = newcell;
    }
    
    return cell;
}

-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    SMedia *media = mediaArray[indexPath.row];
    
    if ([media.mime_type isEqualToString:@"image/png"] ||
        [media.mime_type isEqualToString:@"image/jpeg"] ||
        [media.mime_type isEqualToString:@"image/bmp"] ||
        [media.mime_type isEqualToString:@"image/gif"]) {
        VideoPresentationViewController *vc = [VideoPresentationViewController new];
        
        NSString *urlString = [NSString stringWithFormat:@"%@/resources/%@",BASEURL,media.fileName];
        
        NSError *error = nil;
        NSMutableURLRequest *request = [[NSMutableURLRequest alloc] initWithURL:[NSURL URLWithString:urlString]];
        NSHTTPURLResponse *response;
        [request setHTTPMethod:@"GET"];
        [request setValue:delegate.token forHTTPHeaderField:@"X-Access-Token"];
        
        NSData *responseData = [NSURLConnection sendSynchronousRequest:request returningResponse:&response error:&error];
        
        vc.pictureData = responseData;
        vc.type = @"picture";
        
        [self.navigationController pushViewController:vc animated:YES];
    } else if ([media.mime_type isEqualToString:@"video/quicktime"]) {
        
        NSString *urlString = [NSString stringWithFormat:@"%@/resources/%@",BASEURL,media.fileName];
        
        [self getVideoPathWithURL:urlString];
        
    } else if ([media.mime_type isEqualToString:@"audio/mp4"]) {
        MemoPresentationViewController *vc = [MemoPresentationViewController new];
        
        NSString *urlString = [NSString stringWithFormat:@"%@/resources/%@",BASEURL,media.fileName];
        
        NSError *error = nil;
        NSMutableURLRequest *request = [[NSMutableURLRequest alloc] initWithURL:[NSURL URLWithString:urlString]];
        NSHTTPURLResponse *response;
        [request setHTTPMethod:@"GET"];
        [request setValue:delegate.token forHTTPHeaderField:@"X-Access-Token"];
        
        NSData *responseData = [NSURLConnection sendSynchronousRequest:request returningResponse:&response error:&error];
        
        vc.memoData = responseData;
        
        [self.navigationController pushViewController:vc animated:YES];
    } else if ([media.mime_type isEqualToString:@"text"]) {
        
        MessagePresentationViewController *vc = [MessagePresentationViewController new];
        
        vc.message = media.messageText;
        [self.navigationController pushViewController:vc animated:YES];
    }
}

#pragma mark - spot methods

-(NSMutableArray*)getAllMessages {

    NSMutableArray *medias = [NSMutableArray new];
    
    NSString *urlString = [NSString stringWithFormat:@"%@/bulletin_board/%@/message",BASEURL,self.board.boardID];
    
    NSError *error = nil;
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] initWithURL:[NSURL URLWithString:urlString]];
    NSHTTPURLResponse *response;
    [request setHTTPMethod:@"GET"];
    [request setValue:delegate.token forHTTPHeaderField:@"X-Access-Token"];
    
    NSData *responseData = [NSURLConnection sendSynchronousRequest:request returningResponse:&response error:&error];
    
    NSArray *result = [NSJSONSerialization JSONObjectWithData:responseData options:NSJSONReadingAllowFragments error:&error];
    
    for (NSDictionary *dic1 in result) {
        NSNumber *messageID = [dic1 objectForKey:@"message_id"];
        NSString *messageText = [dic1 objectForKey:@"message_text"];
        NSDictionary *dic2 = [dic1 objectForKey:@"media_wrapper"];
        NSNumber *mediaID = nil;
        NSString *fileName = nil;
        NSString *mimeType = nil;
        if ([dic2 class] == [NSNull class]) {
            mimeType = @"text";
        } else {
            mediaID = [dic2 objectForKey:@"media_id"];
            fileName = [dic2 objectForKey:@"media_file"];
            mimeType = [dic2 objectForKey:@"mime_type"];
        }

        double timestamp = [[dic1 objectForKey:@"created_on"] doubleValue];
        NSDate *date = [NSDate dateWithTimeIntervalSince1970:timestamp/1000];
        
        SMedia *media = [[SMedia alloc] initWithMessageID:messageID messageText:messageText type:mimeType mediaID:mediaID date:date andFileName:fileName];
        
        [medias addObject:media];
    }
    
    return medias;
}

-(void)openUploadView {
    
    UploadViewController *vc = [UploadViewController new];
    vc.board = self.board;
    vc.spot = self.spot;
    [self.navigationController pushViewController:vc animated:YES];
}

-(void)getVideoPathWithURL:(NSString *)urlString
{
    //download the file in a seperate thread.
    dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0), ^{
        
        NSString *filename = [urlString substringFromIndex:[urlString length] - 8];
        
        NSError *error = nil;
        NSMutableURLRequest *request = [[NSMutableURLRequest alloc] initWithURL:[NSURL URLWithString:urlString]];
        NSHTTPURLResponse *response;
        [request setHTTPMethod:@"GET"];
        [request setValue:delegate.token forHTTPHeaderField:@"X-Access-Token"];
        
        NSData *responseData = [NSURLConnection sendSynchronousRequest:request returningResponse:&response error:&error];
        if ( responseData )
        {
            NSArray       *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
            NSString  *documentsDirectory = [paths objectAtIndex:0];
            
            NSString  *filePath = [NSString stringWithFormat:@"%@/%@", documentsDirectory,filename];
            
            //saving is done on main thread
            dispatch_async(dispatch_get_main_queue(), ^{
                [responseData writeToFile:filePath atomically:YES];
                
                VideoPresentationViewController *vc = [VideoPresentationViewController new];
                vc.videoPath = filePath;
                vc.type = @"video";
                
                [self.navigationController pushViewController:vc animated:YES];
            });
        }
        
    });
}

@end
