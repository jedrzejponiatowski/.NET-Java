using System;
using System.Collections.Generic;
using System.Data.Entity;
using System.Linq;
using System.Runtime.Remoting.Contexts;

namespace APP
{
    public class PaymentsContext : DbContext
    {
        // Your context has been configured to use a 'Model1' connection string from your application's 
        // configuration file (App.config or Web.config). By default, this connection string targets the 
        // 'APP.Model1' database on your LocalDb instance. 
        // 
        // If you wish to target a different database and/or database provider, modify the 'Model1' 
        // connection string in the application configuration file.
        public PaymentsContext()
            : base("name=PaymentsContext")
        {
            Database.SetInitializer(new PaymentsDBInitializer());
        }

        public DbSet<Person> Persons { get; set; }
        public DbSet<LoanGranted> LoansGranted { get; set; }
        public DbSet<LoanObtained> LoansObtained { get; set; }

        // Add a DbSet for each entity type that you want to include in your model. For more information 
        // on configuring and using a Code First model, see http://go.microsoft.com/fwlink/?LinkId=390109.

        // public virtual DbSet<MyEntity> MyEntities { get; set; }

        public void AddNewPerson(Person person)
        {
            Persons.Add(person);
            SaveChanges();
        }

        public void DeletePerson(Person person)
        {
            Persons.Attach(person);
            Persons.Remove(person);
            SaveChanges();
        }

        public Person GetFirstPerson() 
        {
            var query = Persons.First();
            return query; 
        }

        public Person GetPerson(string firstName, string lastName)
        {
            var query = Persons.Where(p => p.FirstName == firstName && p.LastName == lastName).FirstOrDefault();
            return query;
        }

        public void AddNewLoanGranted(LoanGranted loan)
        {
            LoansGranted.Add(loan);
            SaveChanges();
        }

        public void AddNewLoanObtained(LoanObtained loan)
        {
            LoansObtained.Add(loan);
            SaveChanges();
        }
        public void PrintAllPersons()
        {
            IList<Person> people;
            people = Persons.ToList();
            foreach (var person in people)
            {
                Console.WriteLine(person.ID + " " + person.FirstName + " " + person.LastName + " " + person.E_Mail + " " + person.PhoneNumber);
            }

        }

        public void PrintAllLoans()
        {
            IList<LoanGranted> loansG;
            loansG = LoansGranted.ToList();
            Console.WriteLine("Loans Granted: ");
            foreach (var loan in loansG)
            {
                Console.WriteLine(loan.LoanGrantedID + " " + loan.Person.FirstName + " " + loan.Person.LastName + " " + loan.LoanSize);
            }

            IList<LoanObtained> loansO;
            loansO = LoansObtained.ToList();
            Console.WriteLine("Loans Obtained: ");
            foreach (var loan in loansO)
            {
                Console.WriteLine(loan.LoanObtainedID + " " + loan.Person.FirstName + " " + loan.Person.LastName + " " + loan.LoanSize);
            }


        }

        public class PaymentsDBInitializer : DropCreateDatabaseAlways<PaymentsContext>
        {
            protected override void Seed(PaymentsContext context)
            {
                IList<Person> defaultPersons = new List<Person>();
                defaultPersons.Add(new Person() { FirstName = "AAA", LastName = "111", E_Mail = "aaa@111.com", PhoneNumber = "123456789" });
                defaultPersons.Add(new Person() { FirstName = "BBB", LastName = "222", E_Mail = "bbb@222.com", PhoneNumber = "234567891" });
                defaultPersons.Add(new Person() { FirstName = "CCC", LastName = "333", E_Mail = "ccc@333.com", PhoneNumber = "345678912" });
                context.Persons.AddRange(defaultPersons);
                base.Seed(context);
            }
        }
    }



    //public class MyEntity
    //{
    //    public int Id { get; set; }
    //    public string Name { get; set; }
    //}
}