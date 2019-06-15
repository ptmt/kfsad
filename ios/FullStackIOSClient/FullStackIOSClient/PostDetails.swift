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
    
    var body: some View {
        Text(postWithComments.post.title).font(.largeTitle)
    }
}

#if DEBUG
struct PostDetails_Previews : PreviewProvider {
    static var previews: some View {
        PostDetails(postWithComments: PostWithComments(post: Post(userId: 1, id: 1, title: "Title", body: "Body"), comments: []))
    }
}
#endif
