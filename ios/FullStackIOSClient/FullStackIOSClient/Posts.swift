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
            ForEach(posts.identified(by: \.post.id)) { (post: PostWithComments) in
                NavigationButton(destination: Text("text")) {
                    PostRow(post: post.post, user: makeUser())
                }
            }.navigationBarTitle(Text("FullStack/iOS"), displayMode: .large)
        }
    }
}

#if DEBUG
struct Posts_Previews : PreviewProvider {
    static var previews: some View {
        Posts(posts: [
            makePostWithComments(),
            makePostWithComments(),
            makePostWithComments()],
              users: [])
    }
}

func makePostWithComments() -> PostWithComments {
return PostWithComments(post: makePost(), comments: [])
}
#endif
