<?php
namespace DashboardHub\Bundle\PipelineDashboardBundle\Service;

/**
 * Class Version
 * @package DashboardHub\Bundle\PipelineDashboardBundle\Service
 */
class VersionService
{
    /**
     * @var string
     */
    protected $file;

    /**
     * @param string $file
     */
    public function __construct($file)
    {
        $this->file = $file;
    }

    /**
     * @return string
     */
    public function current()
    {
        return file_get_contents($this->file);
    }
}
