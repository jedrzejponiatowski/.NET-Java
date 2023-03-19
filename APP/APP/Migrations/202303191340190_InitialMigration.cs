namespace APP.Migrations
{
    using System;
    using System.Data.Entity.Migrations;
    
    public partial class InitialMigration : DbMigration
    {
        public override void Up()
        {
            CreateTable(
                "dbo.LoanGranteds",
                c => new
                    {
                        LoanGrantedID = c.Int(nullable: false, identity: true),
                        LoanSize = c.Int(nullable: false),
                        DateOfIssue = c.String(),
                        DateOfExpiry = c.String(),
                        PersonID = c.Int(nullable: false),
                    })
                .PrimaryKey(t => t.LoanGrantedID)
                .ForeignKey("dbo.People", t => t.PersonID, cascadeDelete: true)
                .Index(t => t.PersonID);
            
            CreateTable(
                "dbo.People",
                c => new
                    {
                        ID = c.Int(nullable: false, identity: true),
                        FirstName = c.String(),
                        LastName = c.String(),
                        E_Mail = c.String(),
                        PhoneNumber = c.String(),
                    })
                .PrimaryKey(t => t.ID);
            
            CreateTable(
                "dbo.LoanObtaineds",
                c => new
                    {
                        LoanObtainedID = c.Int(nullable: false, identity: true),
                        LoanSize = c.Int(nullable: false),
                        DateOfIssue = c.String(),
                        DateOfExpiry = c.String(),
                        PersonID = c.Int(nullable: false),
                    })
                .PrimaryKey(t => t.LoanObtainedID)
                .ForeignKey("dbo.People", t => t.PersonID, cascadeDelete: true)
                .Index(t => t.PersonID);
            
        }
        
        public override void Down()
        {
            DropForeignKey("dbo.LoanObtaineds", "PersonID", "dbo.People");
            DropForeignKey("dbo.LoanGranteds", "PersonID", "dbo.People");
            DropIndex("dbo.LoanObtaineds", new[] { "PersonID" });
            DropIndex("dbo.LoanGranteds", new[] { "PersonID" });
            DropTable("dbo.LoanObtaineds");
            DropTable("dbo.People");
            DropTable("dbo.LoanGranteds");
        }
    }
}
