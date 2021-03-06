package com.github.mongobee.dao;

import static com.github.mongobee.changeset.ChangeEntry.CHANGELOG_COLLECTION;

import org.bson.Document;

import com.github.mongobee.changeset.ChangeEntry;
import com.mongodb.DBCollection;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;

/**
 * @author lstolowski
 * @since 10.12.14
 */
public class ChangeEntryIndexDao {

  public void createRequiredUniqueIndex(MongoCollection<Document> collection) {
    collection.createIndex(new Document()
            .append(ChangeEntry.KEY_CHANGEID, 1)
            .append(ChangeEntry.KEY_AUTHOR, 1),
        new IndexOptions().unique(true)
    );
  }

  public Document findRequiredChangeAndAuthorIndex(MongoDatabase db) {
    MongoCollection<Document> indexes = db.getCollection("system.indexes");
    Document index = indexes.find(new Document()
        .append("ns", db.getName() + "." + CHANGELOG_COLLECTION)
        .append("key", new Document().append(ChangeEntry.KEY_CHANGEID, 1).append(ChangeEntry.KEY_AUTHOR, 1))
    ).first();

    return index;
  }

  public boolean isUnique(Document index) {
    Object unique = index.get("unique");
    if (unique != null && unique instanceof Boolean) {
      return (Boolean) unique;
    } else {
      return false;
    }
  }

  public void dropIndex(MongoCollection<Document> collection, Document index) {
    collection.dropIndex(index.get("name").toString());
  }

}
