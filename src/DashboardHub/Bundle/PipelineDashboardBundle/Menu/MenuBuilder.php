<?php
namespace DashboardHub\Bundle\PipelineDashboardBundle\Menu;

use Knp\Menu\FactoryInterface;
use Symfony\Component\HttpFoundation\RequestStack;
use Symfony\Component\Security\Core\SecurityContext;

/**
 * Class MenuBuilder
 * @package DashboardHub\Bundle\AppBundle\Menu
 */
class MenuBuilder
{
    /**
     * @var FactoryInterface
     */
    private $factory;
    /**
     * @var SecurityContext
     */
    private $securityContext;

    /**
     * @param FactoryInterface $factory
     */
    public function __construct(FactoryInterface $factory, SecurityContext $securityContext)
    {
        $this->factory = $factory;
        $this->securityContext = $securityContext;
    }

    /**
     * @TODO: Move below to config
     *
     * @param RequestStack $requestStack
     *
     * @return \Knp\Menu\ItemInterface
     */
    public function createMainMenu(RequestStack $requestStack)
    {
        $menu = $this->factory->createItem('root');
        $menu->setChildrenAttributes(['class' => 'navbar-nav nav']);
        $menu->addChild('Home', ['route' => 'dashboard_hub_pipeline_dashboard_homepage']);
//        if ($this->securityContext->isGranted('IS_AUTHENTICATED_FULLY')) {
//            $menu->addChild(
//                'Dashboard',
//                ['route' => 'dashboardhub_app_dashboard.list']
//            );
//            $menu->addChild(
//                'Logout ' . $this->securityContext->getToken()
//                    ->getUser()
//                    ->getUsername(),
//                ['route' => 'logout']
//            );
//        } else {
//            $menu->addChild('Login', ['route' => 'login']);
//        }

        return $menu;
    }
}
