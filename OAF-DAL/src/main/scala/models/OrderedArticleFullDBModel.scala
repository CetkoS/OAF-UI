package com.oaf.dal.models


case class OrderedArticleFullDBModel(
                                      orderedArticle: OrderedArticleDBModel,
                                      additives: List[AdditiveDBModel],
                                      articleInfo: ArticleDBModel
                                    )



