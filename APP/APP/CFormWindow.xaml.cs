﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Shapes;

namespace APP
{
    /// <summary>
    /// Interaction logic for CFormWindow.xaml
    /// </summary>
    public partial class CFormWindow : Window
    {
        public CFormWindow()
        {
            InitializeComponent();
        }

        private void btnAddSubmit(object sender, RoutedEventArgs e)
        {
            //string tb1 = TextBox1.Text.Trim();
            //Console.WriteLine(tb1);
        }

        private void btnAddExit(object sender, RoutedEventArgs e)
        {
            this.Close();
        }
    }
}
