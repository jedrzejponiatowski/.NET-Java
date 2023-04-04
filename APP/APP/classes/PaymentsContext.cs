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

        public Person GetPersonWithID(int ID)
        {
            var query = Persons.Where(p => p.ID == ID).FirstOrDefault();
            return query;
        }

        public void AddNewLoanGranted(LoanGranted loan)
        {
            LoansGranted.Add(loan);
            SaveChanges();
        }

        public LoanGranted GetFirstLoanGranted()
        {
            var query = LoansGranted.First();
            return query;
        }

        public LoanGranted GetLoanGrantedwithID(int ID)
        {
            var query = LoansGranted.Where(p => p.LoanGrantedID == ID).FirstOrDefault();
            return query;
        }

        public LoanObtained GetLoanObtainedwithID(int ID)
        {
            var query = LoansObtained.Where(p => p.LoanObtainedID == ID).FirstOrDefault();
            return query;
        }

        public void DeleteLoanGranted(LoanGranted loan)
        {
            LoansGranted.Attach(loan);
            LoansGranted.Remove(loan);
            SaveChanges();
        }

        public void DeleteLoanObtained(LoanObtained loan)
        {
            LoansObtained.Attach(loan);
            LoansObtained.Remove(loan);
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
                defaultPersons.Add(new Person() { FirstName = "Maciej", LastName = "Nowak", E_Mail = "mnowak@op.pl", PhoneNumber = "123456789" });
                defaultPersons.Add(new Person() { FirstName = "Kacper", LastName = "Kowal", E_Mail = "kkowal22@gmail.com", PhoneNumber = "234567891" });
                defaultPersons.Add(new Person() { FirstName = "Adam", LastName = "Ssak", E_Mail = "ssaku123@gmail.com", PhoneNumber = "345678912" });
                context.Persons.AddRange(defaultPersons);
                context.SaveChanges();
                //base.Seed(context);

                context.LoansObtained.Add(new LoanObtained() { PersonID = 1, LoanSize = 123, DateOfIssue = "05.05.2022", DateOfExpiry = "25.05.2022" });
                context.LoansObtained.Add(new LoanObtained() { PersonID = 2, LoanSize = 400, DateOfIssue = "12.05.2021", DateOfExpiry = "12.06.2021" });
                context.LoansGranted.Add(new LoanGranted() { PersonID = 1, LoanSize = 432, DateOfIssue = "01.01.2021", DateOfExpiry = "21.02.2021" });
                context.LoansGranted.Add(new LoanGranted() { PersonID = 2, LoanSize = 96, DateOfIssue = "21.01.2022", DateOfExpiry = "14.03.2022" });
                context.LoansGranted.Add(new LoanGranted() { PersonID = 3, LoanSize = 54, DateOfIssue = "16.01.2020", DateOfExpiry = "11.11.2021" });

                context.SaveChanges();

                base.Seed(context);
            }
        }


        public IList<string> TakeAllPersons()
        {
            IList<Person> people;
            IList<string> peopleOutput = new List<string>();
            people = Persons.ToList();
            foreach (var person in people)
            {
                peopleOutput.Add($"{person.ID}.{person.FirstName} {person.LastName} {person.E_Mail} {person.PhoneNumber}");
            }
            return peopleOutput;
        }



        public IList<string> TakeAllGrants()
        {
            IList<LoanGranted> lGranted;
            IList<string> GrantedOutput = new List<string>();
            lGranted = LoansGranted.ToList();
            foreach (var loan in lGranted)
            {
                //Console.WriteLine($"{loan.Person.ID}.{loan.Person.FirstName}: {loan.LoanSize}PLN, {loan.DateOfExpiry}");
                GrantedOutput.Add($"{loan.LoanGrantedID}.{loan.Person.FirstName}: {loan.LoanSize}PLN, {loan.DateOfExpiry}");
            }
            return GrantedOutput;
        }


        public IList<string> TakeAllObtains()
        {
            IList<LoanObtained> lObtained;
            IList<string> GrantedOutput = new List<string>();
            lObtained = LoansObtained.ToList();
            foreach (var loan in lObtained)
            {
                //Console.WriteLine($"{loan.Person.ID}.{loan.Person.FirstName}: {loan.LoanSize}PLN, {loan.DateOfExpiry}");
                GrantedOutput.Add($"{loan.LoanObtainedID}.{loan.Person.FirstName}: {loan.LoanSize}PLN, {loan.DateOfExpiry}");
            }
            return GrantedOutput;
        }



    }



    //public class MyEntity
    //{
    //    public int Id { get; set; }
    //    public string Name { get; set; }
    //}
}