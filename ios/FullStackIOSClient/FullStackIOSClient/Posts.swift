//
//  Posts.swift
//  FullStackIOSClient
//
//  Created by Dmitriy L on 6/13/19.
//  Copyright © 2019 JetBrains. All rights reserved.
//

import SwiftUI
import SharedLib

struct Posts : View {
    var posts: [PostWithComments]
    var users: [User]
    var body: some View {
        NavigationView {
            List(posts.identified(by: \.post.id)) { (post: PostWithComments) in
                NavigationButton(destination: PostDetails(postWithComments: post, user: makeUser())) {
                    PostRow(post: post, user: makeUser())
                }
            }.navigationBarTitle(Text("FullStack/iOS"), displayMode: .large)
        }
    }
}

#if DEBUG
let postsData = [
    makePostWithComments(1),
    makePostWithComments(2),
    makePostWithComments(3),
    makePostWithComments(4),
    makePostWithComments(5)]
struct Posts_Previews : PreviewProvider {
    static var previews: some View {
        Posts(posts: postsData,
              users: [])
    }
}

func makePostWithComments(_ id: Int) -> PostWithComments {
    return PostWithComments(post: makePost(id: id), comments: [])
}
#endif
