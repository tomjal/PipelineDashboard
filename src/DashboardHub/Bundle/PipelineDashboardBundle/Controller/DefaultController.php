<?php

namespace DashboardHub\Bundle\PipelineDashboardBundle\Controller;

use Symfony\Bundle\FrameworkBundle\Controller\Controller;

class DefaultController extends Controller
{
    public function indexAction()
    {
        return $this->render('DashboardHubPipelineDashboardBundle:Default:index.html.twig');
    }
}
