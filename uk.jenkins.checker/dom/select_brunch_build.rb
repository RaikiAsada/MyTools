# frozen_string_literal: true
require './dom/jenkins_job.rb'
require './dom/jenkins_build.rb'
class SelectBrunchBuild < JenkinsBuild
  def self.load(job)
    Dir.chdir(JenkinsBuild.builds_path(job))
    return Dir.glob('*')
              .select {|file| File.directory?(file)}
              .select { | dir| dir =~ /^\d+$/ }
              .map {|buildDir| SelectBrunchBuild.new(getJobPath, buildDir)}
  end
  def initialize(rootDir, jobName)
    super(rootDir, jobName)
  end
  def get_brunch
    return @buildXml.get_parameter(ParameterType::JENKINS_GIT_PARAM, 'BRANCH')
  end
end