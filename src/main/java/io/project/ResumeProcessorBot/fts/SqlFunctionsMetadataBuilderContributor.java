package io.project.ResumeProcessorBot.fts;


import org.hibernate.boot.MetadataBuilder;
import org.hibernate.boot.spi.MetadataBuilderContributor;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.type.StandardBasicTypes;

public class SqlFunctionsMetadataBuilderContributor implements MetadataBuilderContributor {
    @Override
    public void contribute(MetadataBuilder metadataBuilder) {
        metadataBuilder.applySqlFunction(
                "fts",
                new StandardSQLFunction(
                        "to_tsvector(name) @@ plainto_tsquery",
                        StandardBasicTypes.BOOLEAN
                )
        );
    }


}