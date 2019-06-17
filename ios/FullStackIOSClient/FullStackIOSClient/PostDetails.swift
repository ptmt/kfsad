//
//  PostDetails.swift
//  FullStackIOSClient
//
//  Created by Dmitriy L on 6/13/19.
//  Copyright © 2019 JetBrains. All rights reserved.
//

import SwiftUI
import SharedLib

struct PostDetails : View {
    let postWithComments: PostWithComments
    let user: User
    var body: some View {
        NavigationView {
            VStack(alignment: .center) {
            Text(postWithComments.post.title).font(.largeTitle).lineLimit(4)
                
                HStack(alignment: .center, spacing: 0.0) {
                    Avatar(username: user.username).padding(4.0)
    
                    Text(user.name).font(.subheadline)
                }
                
                Text(postWithComments.post.body)
                                .lineLimit(40)
                                .font(.caption)
                                .padding(3.0)
                
                Text("Comments: \(postWithComments.comments.count)")
                                .font(.caption)
                                .color(Color.secondary)
                                .padding(3.0)
                
                Spacer()
            }.padding(20.0).navigationBarTitle(Text("Post"), displayMode: .inline)
        }



    }
}

#if DEBUG
struct PostDetails_Previews : PreviewProvider {
    static var previews: some View {
        PostDetails(postWithComments: PostWithComments(post: Post(userId: 1, id: 1, title: "Title", body: """
SwiftUI works seamlessly with the existing UI frameworks on all Apple platforms. For example, you can place UIKit views and view controllers inside SwiftUI views, and vice versa.
            
            This tutorial shows you how to convert the featured landmark from the home screen to wrap instances of UIPageViewController and UIPageControl. You’ll use UIPageViewController to display a carousel of SwiftUI views, and use state variables and bindings to coordinate data updates throughout the user interface.
            
            Follow the steps to build this project, or download the finished project to explore on your own.
"""), comments: []), user: makeUser())
    }
}
#endif
